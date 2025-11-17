pipeline {
    agent any

    environment {
        PATH = "/usr/local/bin:/opt/homebrew/bin:${env.PATH}"
    }

    stages {
        stage('Source') {
            steps {
                git 'https://github.com/JuanGuzman-io/unir-cicd'
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
            script {
                if (fileExists('results')) {
                    junit 'results/*_result.xml'
                    archiveArtifacts artifacts: 'results/*.xml', allowEmptyArchive: true, fingerprint: true
                } else {
                    echo 'No hay reportes en results/, se omite publicación.'
                }
            }
            cleanWs()
        }
        failure {
            echo "Fallo en ${env.JOB_NAME} #${env.BUILD_NUMBER} - se enviaría correo de alerta"
        }
    }
}