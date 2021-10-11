Word Wiz

Word wiz is a assistive android application to help users with reading and writing difficulties 
by using a smart dictionary componet to take images of words and show their defintion and to listen to words and show their defintion

The package name is "mltextreader" due to the fact it was orginally based around using machine learning to read the text but
later deccided on a complete solution assistive technology which would employ different api's to give the best user experience

The application uses Firebase's firestore to hold user data, firebase auth using email, Firebases ml kit for ml vision with the ocr value,
Google's text to speech api

Dependices for firebases ml vision is found in the maifest file in the meta data section

API keys are held in the values.xml file and if changing to your own providers you will need to change these

The description popup file is used in both the speech to text and text reader aactivtys to display the word and its defintion using
the Dictionary Request file to make a JSON request for the defintion for oxford dictionary API

The background images of the pages can be  found in the drawables folder

Application needs internet permission actived in the maifest

Recyler view Adapteer is used to show thescrooable saved words section

APK - debug build
located in FinalyearProject>app>debug
key store password - password
allias- ulster1
Key password - password