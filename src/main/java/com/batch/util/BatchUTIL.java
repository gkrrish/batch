package com.batch.util;

import java.util.concurrent.atomic.AtomicBoolean;

public class BatchUTIL {

	public static AtomicBoolean flag = new AtomicBoolean(false);

	public static boolean getFlag() {
		return flag.get();
	}

	public static void setFlag(boolean value) {
		flag.set(value);
	}

	public final static String FROM_EMAIL_ID = "kallemkishan204@gmail.com";

	public static String getMailBody() {
		String emailBody = """
				   <p>Dear Subscriber,</p><p></p>
				   <P>Please find the newspaper attachment.</p>
				   
				   <p></p><p></p>
				   <p>Thanks,<br>
				   Batch Operations<br>
				   NOW-Services India.</p>

					<p style="font-size:10px; font-style:italic;">
				      "This is a Testing email from NOW. For testing purposes, copyrighted content is used under the fair use policy (17 U.S.C. § 107)."
					</p>
				   """;

		emailBody += getEmailHtmlContent();
		return emailBody;
	}

	private static String getEmailHtmlContent() {
		return """
				<!DOCTYPE html>
				<html lang="en">
				<head>
				    <meta charset="UTF-8">
				    <meta name="viewport" content="width=device-width, initial-scale=1.0">
				    <style>
				        body {
				            font-family: Arial, sans-serif;
				            margin: 0;
				            padding: 0;
				        }
				        .banner {
				            background-color: #2da5c8;
				            color: white;
				            text-align: left;
				            padding: 12px;
				            border-radius: 2px;
				            display: inline-block;
				        }
				        .banner p {
				            margin: 0;
				            font-size: 0.8em;
				        }
				    </style>
				</head>
				<body>
				    <div class="banner">
				        <p>ప్రతి ఉషోదయానా ఒక కొత్త సమాచార వేదిక! &nbsp; మేము మీతోనే, ప్రతి న్యూస్ పేపర్ మీ చేతులో....</p>
				    </div>
				</body>
				</html>
				""";
	}

}
