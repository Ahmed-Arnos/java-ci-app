pipeline {
  agent any
  parameters {
    string defaultValue: 'ahmedarnos/java-ci-app', description: 'Docker image name to build & push', name: 'DOCKER_IMAGE'
    string defaultValue: 'latest', description: 'Docker image tag', name: 'IMAGE_TAG'
    string defaultValue: 'dockerhub-creds', description: 'Jenkins credentials ID for DockerHub', name: 'DOCKER_CREDS_ID'
  }

  stages {

    stage('lib'){ steps{ script{
      library identifier:'lab2-cicd@main', retriever: modernSCM([
        $class:'GitSCMSource', remote:'https://github.com/ahmed-arnos/lab2-cicd.git'
      ])
    }}}

    stage('validate'){ steps{ script{ bounds.require(params) }}}

    stage('preflight'){ parallel {
      stage('java'){ steps{ sh 'java -version || true' } }
      stage('mvn'){  steps{ sh 'mvn -v || true' } }
    }}

    stage('build jar'){   steps{ sh 'mvn -B -DskipTests package' } }
    stage('build docker'){steps{ sh "docker build -t ${params.DOCKER_IMAGE}:${params.IMAGE_TAG} ." } }
    stage('login & push'){
      steps {
        withCredentials([usernamePassword(credentialsId: params.DOCKER_CREDS_ID, usernameVariable:'U', passwordVariable:'P')]){
          sh """
            echo "\$P" | docker login -u "\$U" --password-stdin
            docker push ${params.DOCKER_IMAGE}:${params.IMAGE_TAG}
            docker logout || true
          """
        }
      }
    }
  }
  post { always { sh "docker rmi ${params.DOCKER_IMAGE}:${params.IMAGE_TAG} || true"; cleanWs() } }
}
