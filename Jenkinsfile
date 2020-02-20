
node {

    stage('Initialize'){
        def mavenHome  = tool 'myMaven'
        env.PATH = "${mavenHome}/bin:${env.PATH}"
    }

    stage('Checkout') {
        checkout scm
    }

    /*stage('Build'){
            sh "mvn clean install"
    }

     stage('Sonarqube') {
            def scannerHome = tool 'SonarQubeScanner'
            withSonarQubeEnv('sonarqube') {
                   sh "${scannerHome}/bin/sonar-scanner"
            }

            timeout(time: 10, unit: 'MINUTES') {
                    def qg = waitForQualityGate() // Reuse taskId previously collected by withSonarQubeEnv
                    if (qg.status != 'OK') {
                         error "Pipeline aborted due to quality gate failure: ${qg.status}"
                    }
            }
      }*/

    stage('Build and push API to docker registry'){
        withCredentials([usernamePassword(credentialsId: 'DockerhubCredentials', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                    buildAndPush(USERNAME, PASSWORD)
                }
    }

    stage('Deploy on k8s'){
        runApp()
    }

}

def buildAndPush(dockerUser, dockerPassword){
    sh "docker build -t ${env.API_CONTAINER_TAG}  --pull --no-cache ."
    echo "Image build complete"
    sh "docker login -u $dockerUser -p $dockerPassword"
    sh "docker push ${env.API_CONTAINER_TAG}"
    echo "Image push complete"
}


def runApp(){
    sh """
        kubectl set image deployment/zerodash-api zerodash-api=${env.API_CONTAINER_TAG}
        kubectl rollout status -w deployment/zerodash-api
    """
}
