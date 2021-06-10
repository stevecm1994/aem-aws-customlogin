# AEM Project for Custom Login Implementation

This Project Implements functionalities like Signup , ConfirmSignUp , Login , Logout , User Profile Upation , User Details Display with the help of AWS Cognito for user Authentication and AWS DynamoDB for User Profile manupulation.

## Major External Dependencies Used

These dependencies have been added to the projects core pom file.

* AWS Cognito SDKs
* AWS DynamoDB SDKs
* Nimbus SDKs : for jwt token verifications
* Other Compile time dependencies required for the above mentioned.

## AWS Setup required for the project

### AWS Secret Key Set Up

* Open the IAM console at https://console.aws.amazon.com/iam/.

* On the navigation menu, choose Users.

* Choose your IAM user name (not the check box).

* Open the Security credentials tab, and then choose Create access key.

* Download the key pair, choose Download .csv file. Store the .csv file with keys in a secure location.

* Access key can be viewed only at the time of generation , so if key is not saved at the time of creation , then we will have to create it once again.

For More Details refer : https://docs.aws.amazon.com/powershell/latest/userguide/pstools-appendix-sign-up.html#get-access-keys  

### AWS Congnito Set Up

* https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Introduction.html

### AWS DynamoDB Set Up

* https://docs.aws.amazon.com/cognito/latest/developerguide/cognito-user-identity-pools.html

To build all the modules run in the project root directory the following command with Maven 3:

    mvn clean install

If you have a running AEM instance you can build and package the whole project and deploy into AEM with  

    mvn clean install -PautoInstallPackage
    
Or to deploy it to a publish instance, run

    mvn clean install -PautoInstallPackagePublish
    
Or alternatively

    mvn clean install -PautoInstallPackage -Daem.port=4503

Or to deploy only the bundle to the author, run

    mvn clean install -PautoInstallBundle

## Testing

There are three levels of testing contained in the project:

* unit test in core: this show-cases classic unit testing of the code contained in the bundle. To test, execute:

    mvn clean test

* server-side integration tests: this allows to run unit-like tests in the AEM-environment, ie on the AEM server. To test, execute:

    mvn clean verify -PintegrationTests

* client-side Hobbes.js tests: JavaScript-based browser-side tests that verify browser-side behavior. To test:

    in the browser, open the page in 'Developer mode', open the left panel and switch to the 'Tests' tab and find the generated 'MyName Tests' and run them.


## Maven settings

The project comes with the auto-public repository configured. To setup the repository in your Maven settings, refer to:

    http://helpx.adobe.com/experience-manager/kb/SetUpTheAdobeMavenRepository.html
