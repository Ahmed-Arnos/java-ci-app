pipeline {
  agent any

  parameters {
    string defaultValue: 'ahmedarnos/java-ci-app', description: 'Docker image name to build & push', name: 'DOCKER_IMAGE'
    string defaultValue: 'latest',                 description: 'Docker image tag',                  name: 'IMAGE_TAG'
    string defaultValue: 'dockerhub-creds',        description: 'Jenkins credentials ID for DockerHub', name: 'DOCKER_CREDS_ID'
  }

  stages {
    stage('lib'){ steps{ script{
      library identifier:'lab2-cicd@main', retriever: modernSCM([
        $class:'GitSCMSource',
        remote:'https://github.com/ahmed-arnos/lab2-cicd.git'
      ])
    }}}

    stage('validate'){ steps{ script{ bounds.require(params) }}}

    stage('preflight'){ parallel {
      stage('java'){ steps{ bat 'java -version || ver' } }
      stage('mvn'){  steps{ bat 'mvn -v || ver' } }
    }}

    stage('build jar'){ steps{ bat 'mvn -B -DskipTests package' } }

    stage('build docker'){ steps{
      bat "docker build -t ${params.DOCKER_IMAGE}:${params.IMAGE_TAG} ."
    }}

    stage('login & push'){
      steps {
        withCredentials([usernamePassword(credentialsId: params.DOCKER_CREDS_ID, usernameVariable:'U', passwordVariable:'P')]) {
          bat """
          echo %P%| docker login -u %U% --password-stdin
          docker push ${params.DOCKER_IMAGE}:${params.IMAGE_TAG}
          docker logout || ver
          """
        }
      }
    }
  }

  post {
    always {
      bat "docker rmi ${params.DOCKER_IMAGE}:${params.IMAGE_TAG} || ver"
      cleanWs()
    }
  }
}
