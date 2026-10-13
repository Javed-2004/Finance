package com.finance.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtp(
            String email,
            String otp
    ) {

        try {

            MimeMessage message =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            message,
                            true,
                            "UTF-8"
                    );

            helper.setTo(email);

            helper.setSubject(
                    "Verify your email - VoithoX"
            );

            helper.setText(
                    buildOtpEmail(otp),
                    true
            );

            mailSender.send(message);

        } catch (MessagingException e) {

            throw new RuntimeException(
                    "Failed to send OTP email",
                    e
            );
        }
    }


    private String buildOtpEmail(String otp) {

        String html = """
                <!DOCTYPE html>

                <html lang="en">

                <head>

                    <meta charset="UTF-8">

                    <meta
                        name="viewport"
                        content="width=device-width,
                        initial-scale=1.0"
                    >

                    <title>
                        Email Verification
                    </title>

                </head>


                <body style="
                    margin:0;
                    padding:0;
                    background-color:#f4f6f8;
                    font-family:Arial, Helvetica, sans-serif;
                    color:#1f2937;
                ">


                <table
                    width="100%"
                    cellpadding="0"
                    cellspacing="0"
                    border="0"
                    style="
                        background-color:#f4f6f8;
                        padding:40px 15px;
                    "
                >

                    <tr>

                        <td align="center">


                            <!-- EMAIL CARD -->

                            <table
                                width="600"
                                cellpadding="0"
                                cellspacing="0"
                                border="0"
                                style="
                                    max-width:600px;
                                    width:100%;
                                    background:#ffffff;
                                    border-radius:12px;
                                    overflow:hidden;
                                    box-shadow:
                                        0 2px 10px
                                        rgba(0,0,0,0.06);
                                "
                            >


                                <!-- HEADER -->

                                <tr>

                                    <td style="
                                        padding:28px 35px;
                                        border-bottom:
                                            1px solid #eeeeee;
                                    ">

                                        <div style="
                                            font-size:25px;
                                            font-weight:700;
                                            color:#111827;
                                        ">
                                            VoithoX
                                        </div>

                                        <div style="
                                            margin-top:4px;
                                            font-size:11px;
                                            color:#6b7280;
                                            letter-spacing:1px;
                                        ">
                                            TECHNOLOGIES
                                            PVT. LTD.
                                        </div>

                                    </td>

                                </tr>


                                <!-- CONTENT -->

                                <tr>

                                    <td style="
                                        padding:40px 35px 30px;
                                    ">


                                        <h1 style="
                                            margin:0 0 15px;
                                            font-size:26px;
                                            line-height:1.3;
                                            color:#111827;
                                        ">

                                            Verify your email

                                        </h1>


                                        <p style="
                                            margin:0 0 25px;
                                            font-size:15px;
                                            line-height:1.7;
                                            color:#4b5563;
                                        ">

                                            We received a request
                                            to verify your email
                                            address.

                                            Use the verification
                                            code below to continue.

                                        </p>


                                        <!-- OTP BOX -->

                                        <table
                                            width="100%"
                                            cellpadding="0"
                                            cellspacing="0"
                                        >

                                            <tr>

                                                <td
                                                    align="center"
                                                    style="
                                                        padding:25px;
                                                        background:#f8fafc;
                                                        border:
                                                            1px solid
                                                            #e5e7eb;
                                                        border-radius:10px;
                                                    "
                                                >

                                                    <div style="
                                                        font-size:11px;
                                                        color:#6b7280;
                                                        margin-bottom:10px;
                                                        text-transform:
                                                            uppercase;
                                                        letter-spacing:
                                                            1.5px;
                                                    ">

                                                        Verification Code

                                                    </div>


                                                    <div style="
                                                        font-size:36px;
                                                        font-weight:700;
                                                        letter-spacing:8px;
                                                        color:#111827;
                                                    ">

                                                        {{OTP}}

                                                    </div>

                                                </td>

                                            </tr>

                                        </table>


                                        <!-- EXPIRY -->

                                        <p style="
                                            margin:25px 0 0;
                                            font-size:14px;
                                            line-height:1.6;
                                            color:#4b5563;
                                        ">

                                            This verification code
                                            will expire in

                                            <strong>
                                                5 minutes
                                            </strong>.

                                        </p>


                                        <!-- SECURITY NOTICE -->

                                        <table
                                            width="100%"
                                            cellpadding="0"
                                            cellspacing="0"
                                            style="
                                                margin-top:25px;
                                            "
                                        >

                                            <tr>

                                                <td style="
                                                    padding:15px 18px;
                                                    background:#fff7ed;
                                                    border-left:
                                                        4px solid
                                                        #f97316;
                                                    border-radius:5px;
                                                ">


                                                    <p style="
                                                        margin:0;
                                                        font-size:13px;
                                                        line-height:1.6;
                                                        color:#7c2d12;
                                                    ">

                                                        <strong>
                                                            Security notice:
                                                        </strong>

                                                        Never share this
                                                        verification code
                                                        with anyone.

                                                        VoithoX Technologies
                                                        will never ask you
                                                        for your OTP.

                                                    </p>


                                                </td>

                                            </tr>

                                        </table>


                                        <!-- UNKNOWN REQUEST -->

                                        <p style="
                                            margin:28px 0 0;
                                            font-size:13px;
                                            line-height:1.6;
                                            color:#6b7280;
                                        ">

                                            If you did not request
                                            this verification code,
                                            you can safely ignore
                                            this email.

                                        </p>


                                    </td>

                                </tr>


                                <!-- FOOTER -->

                                <tr>

                                    <td style="
                                        padding:25px 35px;
                                        background:#f9fafb;
                                        border-top:
                                            1px solid #eeeeee;
                                    ">


                                        <p style="
                                            margin:0 0 8px;
                                            font-size:13px;
                                            font-weight:600;
                                            color:#374151;
                                        ">

                                            VoithoX Technologies
                                            Pvt. Ltd.

                                        </p>


                                        <p style="
                                            margin:0 0 8px;
                                            font-size:12px;
                                            line-height:1.6;
                                            color:#6b7280;
                                        ">

                                            Shahpur Dasar,
                                            Pantya Mafi,<br>

                                            Uttar Pradesh
                                            244102, India

                                        </p>


                                        <p style="
                                            margin:0;
                                            font-size:12px;
                                            color:#9ca3af;
                                        ">

                                            This is an automated email.
                                            Please do not reply directly
                                            to this message.

                                        </p>


                                    </td>

                                </tr>


                            </table>


                            <!-- COPYRIGHT -->

                            <p style="
                                margin:20px 0 0;
                                font-size:11px;
                                color:#9ca3af;
                                text-align:center;
                            ">

                                © 2026 VoithoX Technologies Pvt. Ltd.
                                All rights reserved.

                            </p>


                        </td>

                    </tr>

                </table>


                </body>

                </html>
                """;

        /*
         * Replace OTP placeholder.
         *
         * We intentionally DO NOT use String.formatted()
         * because the HTML/CSS contains '%' characters.
         */
        return html.replace(
                "{{OTP}}",
                otp
        );
    }
}
