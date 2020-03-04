def label = "worker-${UUID.randomUUID().toString()}"

podTemplate(label: label, containers: [
        containerTemplate(name: 'maven', image: 'maven:3.3.9-jdk-8-alpine', command: 'cat', ttyEnabled: true),
        containerTemplate(name: 'docker', image: 'docker', command: 'cat', ttyEnabled: true),
        containerTemplate(name: 'kubectl', image: 'roffe/kubectl', command: 'cat', ttyEnabled: true)
],
        volumes: [
                hostPathVolume(mountPath: '/root/.m2', hostPath: '/home/jenkins/.m2'),
                hostPathVolume(mountPath: '/var/run/docker.sock', hostPath: '/var/run/docker.sock')
        ]) {

    node('chilling-jenkins-jenkins-slave') {
        stage('Checkout') {
            checkout scm
        }

        stage('Build') {
            container('maven') {
                sh "mvn clean install -DskipTests=true"
            }

        }

        /*stage('Sonarqube') {
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

        withEnv(["api_image_tag=imzerofiltre/zerodash-api:0.0.${env.BUILD_NUMBER}"]) {
            stage('Build and push API to docker registry') {
                withCredentials([usernamePassword(credentialsId: 'DockerHubCredentials', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                    buildAndPush(USERNAME, PASSWORD)
                }
            }

            stage('Deploy on k8s') {
                runApp()
            }

        }
    }
}


def buildAndPush(dockerUser, dockerPassword) {
    container('docker') {
        sh """
                docker build -t ${api_image_tag}  --pull --no-cache .
                echo "Image build complete"
                docker login -u $dockerUser -p $dockerPassword
                docker push ${api_image_tag}
                echo "Image push complete"

         """

    }

}

def runApp() {

    container('kubectl') {
        dir("k8s") {
            sh """
                     kubectl apply -f api-service.yaml
                     kubectl apply -f api-secret.yaml
                     kubectl apply -f api-deployment.yaml
                   """
        }
        sh """
                kubectl set image deployment/zerodash-api zerodash-api=${api_image_tag}
                if ! kubectl rollout status -w deployment/zerodash-api; then
                    kubectl rollout undo deployment/zerodash-api
                    kubectl rollout status deployment/zerodash-api
                    exit 1
                fi
            """
    }

}
