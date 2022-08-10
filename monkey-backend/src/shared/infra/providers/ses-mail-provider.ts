import {
  IMailProvider,
  SendMailDTO,
} from '@shared/domain/providers/mail-provider';
import { IMailTemplateProvider } from '@shared/domain/providers/mail-template-provider';
import aws from 'aws-sdk';
import { createTransport, Transporter } from 'nodemailer';
import { inject, injectable } from 'tsyringe';
import { mailConfig } from '../../../config/mail';

@injectable()
export class SESMailProvider implements IMailProvider {
  private transporter: Transporter;

  constructor(
    @inject('MailTemplateProvider')
    private mailTemplateProvider: IMailTemplateProvider,
  ) {
    const transporter = createTransport({
      SES: new aws.SES({
        apiVersion: '2010-12-01',
        region: 'us-east-1',
      }),
    });

    this.transporter = transporter;
  }

  async sendMail({
    to,
    from,
    subject,
    template_data,
  }: SendMailDTO): Promise<void> {
    const { name, address } = mailConfig.config.ses.defaults.from;

    await this.transporter.sendMail({
      from: {
        address: from?.address || address,
        name: from?.name || name,
      },
      to: {
        name: to.name,
        address: to.address,
      },
      subject,
      html: await this.mailTemplateProvider.parse(template_data),
    });
  }
}
