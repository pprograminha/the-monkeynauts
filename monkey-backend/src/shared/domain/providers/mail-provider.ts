import { ParseMailTemplateDTO } from './mail-template-provider';

type MailContact = {
  name: string;
  address: string;
};
type SendMailDTO = {
  to: MailContact;
  from?: MailContact;
  subject: string;
  template_data: ParseMailTemplateDTO;
};

interface IMailProvider {
  sendMail(data: SendMailDTO): Promise<void>;
}

export { SendMailDTO, IMailProvider };
