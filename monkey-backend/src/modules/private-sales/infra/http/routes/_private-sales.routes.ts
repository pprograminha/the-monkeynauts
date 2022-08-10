import { balanceConfig } from '@config/balance';
import { txHashRegExp } from '@config/regexp';
import { adaptRoute } from '@shared/core/infra/adapters/express-route-adapter';
import { celebrate, Joi, Segments } from 'celebrate';
import { Router } from 'express';
import { createPrivateSaleController } from '../controllers/create-private-sale';

const _privateSalesRouter = Router();

_privateSalesRouter.post(
  '/create-private-sale',
  celebrate(
    {
      [Segments.BODY]: {
        wallet: Joi.string().required(),
        bnbAmount: Joi.number()
          .required()
          .min(balanceConfig.bnbAmountMin)
          .max(balanceConfig.bnbAmountMax),
        txHash: Joi.string().required().regex(txHashRegExp),
      },
    },
    {
      abortEarly: false,
    },
  ),
  adaptRoute(createPrivateSaleController),
);

_privateSalesRouter.use('/private-sales', _privateSalesRouter);

export { _privateSalesRouter };
