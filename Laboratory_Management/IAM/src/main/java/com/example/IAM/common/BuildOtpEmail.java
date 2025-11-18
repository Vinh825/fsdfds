package com.example.IAM.common;

import java.time.Year;

public class BuildOtpEmail {

    public static String buildOtpEmailHtml(String code) {

        return """
                                <!doctype html>
                                <html lang="en">
                                  <head>
                                    <meta charset="utf-8">
                                    <meta name="x-apple-disable-message-reformatting">
                                    <meta name="viewport" content="width=device-width, initial-scale=1">
                                    <title>Your OTP Code</title>
                                  </head>
                                  <body style="margin:0;padding:0;background:#f6f7f9;">
                                    <!-- Preheader (hidden) -->
                                    <div style="display:none;max-height:0;overflow:hidden;opacity:0;">
                                      Your password reset OTP · Valid for 5 minutes
                                    </div>
                
                                    <!-- Wrapper -->
                                    <table role="presentation" cellpadding="0" cellspacing="0" width="100%%" style="background:#f6f7f9;">
                                      <tr>
                                        <td align="center" style="padding:24px 12px;">
                                          <!-- Card -->
                                          <table role="presentation" cellpadding="0" cellspacing="0" width="100%%" style="max-width:640px;background:#ffffff;border-radius:12px;border:1px solid #e9edf3;">
                                            <!-- Header -->
                                            <tr>
                                              <td align="center" style="padding:28px 24px 0 24px;">
                                                <div style="font-family:Arial,Helvetica,sans-serif;font-size:28px;line-height:1.2;font-weight:700;color:#27ae60;">
                                                          Healthcare Laboratory 
                                                </div>
                                                <div style="font-family:Arial,Helvetica,sans-serif;color:#5f6b7a;font-size:13px;margin-top:6px;">
                                                  Keep your account secure
                                                </div>
                                              </td>
                                            </tr>
                
                                            <!-- Title -->
                                            <tr>
                                              <td align="center" style="padding:24px 24px 0 24px;">
                                                <div style="font-family:Arial,Helvetica,sans-serif;font-size:22px;line-height:1.3;font-weight:700;color:#0b1f33;">
                                                  Verify your identity
                                                </div>
                                              </td>
                                            </tr>
                
                                            <!-- Body text -->
                                            <tr>
                                              <td align="center" style="padding:12px 28px 0 28px;">
                                                <div style="font-family:Arial,Helvetica,sans-serif;color:#334155;font-size:14px;line-height:22px;">
                                                  We received a request to reset your password. Use the verification code below to continue.
                                                </div>
                                              </td>
                                            </tr>
                
                                            <!-- OTP box -->
                                            <tr>
                                              <td align="center" style="padding:20px 28px 0 28px;">
                                                <table role="presentation" cellpadding="0" cellspacing="0" style="border-collapse:separate;border-spacing:0;">
                                                  <tr>
                                                    <td style="padding:16px 24px;border:1px dashed #cbd5e1;border-radius:10px;background:#f8fafc;">
                                                      <div style="font-family:SFMono-Regular,Menlo,Monaco,Consolas,'Liberation Mono','Courier New',monospace;font-size:28px;letter-spacing:6px;font-weight:700;color:#0b1f33;">
                                                        %1$s
                                                      </div>
                                                    </td>
                                                  </tr>
                                                </table>
                                              </td>
                                            </tr>
                
                                            <!-- Expiry + note -->
                                            <tr>
                                              <td align="center" style="padding:18px 28px 0 28px;">
                                                <div style="font-family:Arial,Helvetica,sans-serif;color:#475569;font-size:13px;line-height:20px;">
                                                  This code is valid for <b>5 minutes</b> and can be used once.
                                                </div>
                                                <div style="font-family:Arial,Helvetica,sans-serif;color:#64748b;font-size:12px;line-height:20px;margin-top:4px;">
                                                  If you didn’t request this, you can safely ignore this email.
                                                </div>
                                              </td>
                                            </tr>
                
                                            <!-- Divider -->
                                            <tr>
                                              <td style="padding:24px 24px 0 24px;">
                                                <hr style="border:none;border-top:1px solid #eef2f7;margin:0;">
                                              </td>
                                            </tr>
                
                                            <!-- Footer -->
                                            <tr>
                                              <td align="center" style="padding:16px 24px 28px 24px;">
                                                <div style="font-family:Arial,Helvetica,sans-serif;color:#94a3b8;font-size:12px;line-height:18px;">
                                                  © %2$d Laboratory Management • Automated message – please do not reply
                                                </div>
                                              </td>
                                            </tr>
                                          </table>
                                          <!-- /Card -->
                                        </td>
                                      </tr>
                                    </table>
                                  </body>
                                </html>
                """.formatted(code, Year.now().getValue());
    }
}
