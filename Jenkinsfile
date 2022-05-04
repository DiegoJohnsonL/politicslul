pipeline {
    agent any
    tools { 
        maven 'MAVEN_3_6_3' 
        jdk 'JAVA_HOME' 
    }
	
    stages {
        stage ('Compile Stage 2021-2') {

            steps {
                withMaven(maven : 'MAVEN_3_6_3') {
                    bat 'mvn clean compile'
                }
            }
        }

        stage ('Testing Stage') {

            steps {
                withMaven(maven : 'MAVEN_3_6_3') {
                    bat 'mvn test'
                }
            }
        }


        stage ('package Stage') {
            steps {
                withMaven(maven : 'MAVEN_3_6_3') {
                    bat 'mvn package'
                }
            }
        }

    }
}
