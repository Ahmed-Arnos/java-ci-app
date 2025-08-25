pipeline {
  agent any
  parameters {
    string(name:'DOCKER_IMAGE',    defaultValue:'ahmedarnos/java-ci-app')
    string(name:'IMAGE_TAG',       defaultValue:'latest')
    string(name:'DOCKER_CREDS_ID', defaultValue:'dockerhub-creds')
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
