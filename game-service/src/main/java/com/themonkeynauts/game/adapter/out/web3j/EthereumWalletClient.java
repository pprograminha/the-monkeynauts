package com.themonkeynauts.game.adapter.out.web3j;

import com.themonkeynauts.game.application.port.out.blockchain.WalletClientPort;
import com.themonkeynauts.game.common.annotation.WalletAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.web3j.contracts.eip20.generated.ERC20;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Numeric;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@WalletAdapter
public class EthereumWalletClient implements WalletClientPort {

    private static final Pattern ignoreCaseAddrPattern = Pattern.compile("(?i)^(0x)?[0-9a-f]{40}$");
    private static final Pattern lowerCaseAddrPattern = Pattern.compile("^(0x)?[0-9a-f]{40}$");
    private static final Pattern upperCaseAddrPattern = Pattern.compile("^(0x)?[0-9A-F]{40}$");

    private final Web3j client;
    private final Credentials credentials;
    private final ERC20 token;

    private final String privateKey;
    private final String contractAddress;

    public EthereumWalletClient(Web3j client, @Value("${wallet.contract.privateKey}") String privateKey, @Value("${wallet.contract.id}") String contractAddress) {
        this.client = client;
        this.privateKey = privateKey;
        this.contractAddress = contractAddress;
        this.credentials = Credentials.create(this.privateKey);
        var gasLimit = DefaultGasProvider.GAS_LIMIT;
        var gasPrice = DefaultGasProvider.GAS_PRICE;
        try {
            gasPrice = client.ethGasPrice().sendAsync().get(10, TimeUnit.SECONDS).getGasPrice();
        } catch (Exception e) {}
        this.token = ERC20.load(this.contractAddress, this.client, credentials, new StaticGasProvider(gasPrice, gasLimit));
    }

    @Override
    public boolean isValidAddress(String address) {
        if (address.isEmpty() || !ignoreCaseAddrPattern.matcher(address).find()) {
            return false;
        }
        if (lowerCaseAddrPattern.matcher(address).find() || upperCaseAddrPattern.matcher(address).find()) {
            return true;
        }
        return validateChecksumAddress(address);
    }

    private boolean validateChecksumAddress(String address) {
        var addressPostfix = address.replace("0x", "");
        var hash = Numeric.toHexStringNoPrefix(Hash.sha3(addressPostfix.toLowerCase().getBytes()));
        for (int i = 0; i < 40; i++) {
            if (Character.isLetter(addressPostfix.charAt(i))) {
                int charInt = Integer.parseInt(Character.toString(hash.charAt(i)), 16);
                if ((Character.isUpperCase(addressPostfix.charAt(i)) && charInt <= 7)
                        || (Character.isLowerCase(addressPostfix.charAt(i)) && charInt > 7)) {
                    return false;
                }
            }
        }
        return true;
    }
}
