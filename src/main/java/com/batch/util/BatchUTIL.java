package com.batch.util;

public class BatchUTIL {

	public static boolean flag = false;
	
	
	public final static String FROM_EMAIL_ID = "kallemkishan204@gmail.com";

	public static String getMailBody() {
		String emailBody = """
				Dear Subscriber,
				Please find the newspaper attachment.
				""";
		emailBody += getEmailHtmlContent();

		emailBody += """
				Thanks,<br>Batch Operations<br>NOW-Services India.
				""";
		return emailBody;
	}

	private static String getEmailHtmlContent() {
		return """
					    		<!DOCTYPE html>
				<html lang="en">
				<head>
				    <meta charset="UTF-8">
				    <meta name="viewport" content="width=device-width, initial-scale=1.0">
				    <title>Large Banner</title>
				    <style>
				        body {
				            font-family: Arial, sans-serif;
				            margin: 0;
				            padding: 0;
				        }
				        .banner {
				            background-color: #4CAF50;
				            color: white;
				            text-align: center;
				            padding: 3px; /* Increased padding */
				            border-radius: 3px;
				            margin: 5px; /* Increased margin */
				            max-width: 550px; /* Increased max-width */
				            margin: auto;
				        }
				        .banner h1 {
				            margin: 0;
				            font-size: 1.0em; /* Increased font size */
				        }
				        .banner p {
				            margin: 3px 0 5px;
				            font-size: 1.0em; /* Increased font size */
				        }
				        .banner a {
				            background-color: white;
				            color: #4CAF50;
				            padding: 5px 10px; /* Increased padding */
				            text-decoration: none;
				            border-radius: 3px;
				            font-size: 1.0em; /* Increased font size */
				            transition: background-color 0.3s, color 0.3s;
				        }
				        .banner a:hover {
				            background-color: #45a049;
				            color: white;
				        }
				    </style>
				</head>
				<body>
				    <div class="banner">
				        <h2>ప్రతి ఉషోదయానా ఒక కొత్త సమాచార వేదిక !</h2>
				        <p>మేము మీతోనే, ప్రతి న్యూస్ పేపర్ మీ చేతులో ....</p>

				    </div>
				</body>
				</html>

					    		""";
	}

}
