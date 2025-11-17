pipeline {
    agent { label 'docker' }

    stages {
        stage('Source') {
            steps {
                git 'https://github.com/srayuso/unir-cicd.git'
            }
        }

        stage('Build') {
            steps {
                echo 'Building stage!'
                sh 'make build'
            }
        }

        stage('Unit tests') {
            steps {
                sh 'make test-unit'
            }
        }

        stage('API tests') {
            steps {
                sh 'make test-api'
            }
        }

        stage('E2E tests') {
            steps {
                sh 'make test-e2e'
            }
        }
    }

    post {
        always {
            junit 'results/*_result.xml'
            archiveArtifacts artifacts: 'results/*.xml', allowEmptyArchive: true, fingerprint: true
            cleanWs()
        }
        failure {
            echo "Fallo en ${env.JOB_NAME} #${env.BUILD_NUMBER} - se enviaría correo de alerta"
            // mail to: 'tu-correo@example.com', subject: "Fallo en ${env.JOB_NAME} #${env.BUILD_NUMBER}", body: "El pipeline ${env.JOB_NAME} falló en la ejecución #${env.BUILD_NUMBER}."
        }
    }
}
