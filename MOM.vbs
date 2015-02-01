'creéation du shell pour éxécuter d'autres script
Set objShell = CreateObject("WScript.Shell")
'création du fichier
Set objFSO = CreateObject("Scripting.FileSystemObject")
'création du xmldom
Set xmlDoc = CreateObject("Microsoft.XMLDOM")

'on charge tout le fichier en mémoire
xmlDoc.Async = "false"
'on load le fichier de conf
xmlDoc.Load("config.xml")
'on définit le root du xml
Set Root = xmlDoc.documentElement

'le nom donnée à l'AVD
Dim avdName
'boolean statuant la création d'une sdcard
Dim createSdCard
'chemin de l'appli, du sdk, et des logs
Dim appPath
Dim sdkPath
Dim logPath
'destinataires du mail
Dim recipientsMail
Dim recipientsMailError
'Contenu du mail
body = Chr(34) & "If there is any attachment, it means the test failed. You can find the logs attached." & Chr(34)

'---------------------------------- PARSE DU XML DE CONFIG ----------------------------------------

'on charge le nom de l'avd
avdname = Root.selectSingleNode("avdname").text
'on charge le boolean statuant la création d'une sdcard
createSdCard = Root.selectSingleNode("createsdcard").text
'on charge les chemins d'accès à l'application, au SDK, et au dossier des Logs
appPath = Root.selectSingleNode("pathapp").text
sdkPath = Root.selectSingleNode("pathsdk").text
logPath = appPath & "\Log\" & Year(Date) & "_" & Right("0" & DatePart("m",Date), 2) & "_" & Right("0" & DatePart("d",Date), 2)
'on charge les données du fichier de conf
dir =  Root.selectSingleNode("dir").text
smtpsender =  Root.selectSingleNode("smtpsender").text
smtpserver =  Root.selectSingleNode("smtpserver").text
smtpport =  Root.selectSingleNode("smtpport").text
subject =  Root.selectSingleNode("subject").text
'parcours de node d'une collection - recupère les adresses mail
For Each recipients In xmlDoc.selectNodes("/config/recipients/recipient")
	i=i+1
	email =  recipients.selectSingleNode("address").text
	If (i=1) Then
		recipientsMail= Chr(34) &  + email + Chr(34) 
	Else
		recipientsMail=recipientsMail & "," & Chr(34) & email & Chr(34)
	End If
Next

'---------------------------- LANCEMENT DE LA CREATION DE SDCARD et/ou DU TEST ---------------------------

'on définit le script à lancer en fonction de si on créé une sdcard ou non
Dim scriptToLaunch
If (createSdCard = "true") Then	 
	scriptToLaunch = "Scripts\CreateSdCard.bat "
Else
	scriptToLaunch = "Scripts\LaunchTest.bat "
End If

'exécution d'un .bat via le shell créé - le true signifie que le .vbs attend la fin de l'éxécution du bat pour continuer. 
'On peut mettre false pour éxécuter plusieurs bat en parallèle
objShell.Run scriptToLaunch & appPath & " " & sdkPath & " " & logPath & " " & avdname , , True 

'si l'étape précédente a requiert la création d'une sdcard, on lance maintenant le test
If (createSdCard = "true") Then
  objShell.Run "Scripts\LaunchTest.bat " & appPath & " " & sdkPath & " " & logPath & " " & avdname , , True
End If


'---------------------------------- VERIFICATION DU RESULTAT DU TEST ----------------------------------

'on ouvre le fichier de log d'erreur
Set objFile = objFSO.OpenTextFile(logPath & "\log_ERREUR.txt", ForReading)
Const ForReading = 1
'déclaration d'une array contenant les lignes du fichiers
Dim arrFileLines()
'variable pour l'itération
i = 0
'variable stockant le résultat du test, intialisée à 1 (échec)
Dim isTestFailed
isTestFailed = 1
'pour chaque ligne du fichier, on la met dans l'array créée
Do Until objFile.AtEndOfStream
	Redim Preserve arrFileLines(i)
		If (InStr(1,objFile.ReadLine,"FAILURE") = "1") Then
			isTestFailed = 1
			WScript.echo "Test failed"
		End If
	i = i + 1
	Loop
'on ferme le fichier de log d'erreur
objFile.Close

'si le test à échouer
If (isTestFailed = 1) Then
	'Zip de dossier contenant les logs
	objShell.Run "Scripts\zipFolder.vbs " & logPath & ".zip " & logPath , , True
	'Envoi du mail
	objShell.Run dir & " -body " & body & " -server " & smtpserver & " -f " & smtpsender & " -subject " & subject & " -to " & recipientsMail & " -attach " & Chr(34) & logPath & ".zip" & Chr(34), 3, 1
Else
	'Envoi du mail
	objShell.Run dir & " -body " & body & " -server " & smtpserver & " -f " & smtpsender & " -subject " & subject & " -to " & recipientsMail, 3, 1
End If

'on détruit le shell et le xmlDoc à la fin du script
Set xmlDoc = Nothing
Set objShell = Nothing
Set objFSO = Nothing
Set objFile = Nothing

