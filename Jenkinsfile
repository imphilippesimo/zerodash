def label = "worker-${UUID.randomUUID().toString()}"

podTemplate(label: label, containers: [
  containerTemplate(name: 'maven', image: 'maven:3.3.9-jdk-8-alpine', command: 'cat', ttyEnabled: true),
  containerTemplate(name: 'docker', image: 'docker', command: 'cat', ttyEnabled: true),
  containerTemplate(name: 'kubectl', image: 'lachlanevenson/k8s-kubectl:v1.8.8', command: 'cat', ttyEnabled: true),
  containerTemplate(name: 'helm', image: 'lachlanevenson/k8s-helm:latest', command: 'cat', ttyEnabled: true)
],
volumes: [
  hostPathVolume(mountPath: '/root/.m2/repository', hostPath: '/tmp/jenkins/.m2/repository'),
  hostPathVolume(mountPath: '/var/run/docker.sock', hostPath: '/var/run/docker.sock')
]){
    node(label){

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
                      withCredentials([usernamePassword(credentialsId: 'DockerHubCredentials', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                                  buildAndPush(USERNAME, PASSWORD)
                              }
               }

               stage('Deploy on k8s'){
                       runApp()
               }

    }
}

def buildAndPush(dockerUser, dockerPassword){
    container('docker'){
        sh "docker build -t ${env.API_CONTAINER_TAG}  --pull --no-cache ."
        echo "Image build complete"
        sh "docker login -u $dockerUser -p $dockerPassword"
        sh "docker push ${env.API_CONTAINER_TAG}"
        echo "Image push complete"
    }

}

def runApp(){

    container('kubectl'){

        if (${env.API_CONTAINER_TAG} == 'imzerofiltre/zerodash-api:1.0.0') {
            dir("k8s") {
                   sh """
                     envsubst ${env.API_CONTAINER_TAG} < api-deployment.yaml > api.tmp.yaml
                     kubectl create -f api-service.yaml
                     kubectl create -f api-secret.yaml
                     kubectl create -f api.tmp.yaml
                   """
            }
        }
        sh """
                kubectl set image deployment/zerodash-api zerodash-api=${env.API_CONTAINER_TAG}
                kubectl rollout status -w deployment/zerodash-api
            """
    }

}
