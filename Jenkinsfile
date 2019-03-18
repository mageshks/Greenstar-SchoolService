/* This script is coded to run in windows environment
   dockerx is the bat file name configured in windows docker client installed path
   Steps to setup docker client and host
   =====================================
   1. Download “Docker client for Linux, MacOS and Windows”
   2. Navigate in the folder and create "dockerx.bat" file with following contents:
      Docker -H=172.18.2.50:2375  %*
   3. Save the file and close it
 */
node {
    withMaven(maven:'localmaven') {

        stage('Checkout') {
            git url: 'http://172.18.2.18/teamgreenstar/371793-Hackathon-SchoolService.git', credentialsId: 'mageshgitlabcred', branch: 'master'
        }
        stage('Build') {
		  bat label: 'Maven Build status', script: 'mvn clean install -DskipTests'
        }
        stage('Test') {
		  bat label: 'Maven Test', returnStatus: true, script: 'mvn test'
        }
        stage('Docker Image') {
            bat label: 'Docker Image',script: 'dockerx build --tag=greenstarapp-school-service:latest --rm=true .'
        }
        stage('Stop And Remove Container Exists') {
            /*To stop and remove if container exists or running. 
			 Returning true to avoid failure if not exists or running */
            bat label: 'Stop/Remove if exists', returnStatus: true, script: 'dockerx stop greenstarapp-school-service-container && dockerx rm greenstarapp-school-service-container'
        }
        stage('Docker Run') {
            bat label: 'Docker Run status', script: 'dockerx run --name=greenstarapp-school-service-docker -d --publish=2620:2620 -e eureka.client.serviceUrl.defaultZone="http://172.18.2.50:8761/eureka/" -e spring.datasource.url="jdbc:mysql://greenstardb:3306/greenstar?useSSL=false" --link greenstardb:mysql greenstarapp-school-service:latest'
        }
    }
}