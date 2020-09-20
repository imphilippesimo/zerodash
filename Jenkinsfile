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

    node(label) {
        stage('Checkout') {
            checkout scm
        }

        stage('Build') {
            container('maven') {
                sh "mvn clean install"
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

        withEnv(["api_image_tag=imzerofiltre/zerodash-api:${env.BUILD_NUMBER}",
                 "env_name=${env.BRANCH_NAME == "master" ? "prod" : "dev"}"
        ]) {
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
                  echo "Branch:" ${env.BRANCH_NAME}
                  echo "env:" ${env_name}
                  kubectl apply -f api-${env_name}.yaml
               """
        }
        sh """
                kubectl set image deployment/zerodash-${env_name} zerodash-${env_name}=${api_image_tag} -n zerodash-${env_name}
                if ! kubectl rollout status -w deployment/zerodash-${env_name} -n zerodash-${env_name}; then
                    kubectl rollout undo deployment.v1.apps/zerodash-${env_name} -n zerodash-${env_name}
                    kubectl rollout status deployment/zerodash-${env_name} -n zerodash-${env_name}
                    exit 1
                fi
            """
    }

}
