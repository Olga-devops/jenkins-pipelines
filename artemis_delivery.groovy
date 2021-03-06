node {
	properties(
		[parameters(
			[choice(choices: 
			[
				'0.1', 
				'0.2', 
				'0.3', 
				'0.4', 
				'0.5'], 
	description: 'Which version of the app should I deploy? ', 
	name: 'Version')])])
	stage("Stage1"){
		timestamps {
			ws {
		          checkout([$class: 'GitSCM', branches: [[name:'$Version']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/farrukh90/artemis.git']]])
		}
	}
}
	stage("Get Credentials"){
		timestamps {
			ws{
				sh '''
					aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 528547571480.dkr.ecr.us-east-1.amazonaws.com/artemis
					'''
		    }
	    }
    }
	stage("Build Docker Image"){ 
          timestamps { 
            ws { 
              sh ''' 
                docker build -t artemis:${Version} . 
           ''' 
      } 
    }
  } 
	stage("Tag Image"){ 
          timestamps { 
            ws { 
            sh ''' 
            docker tag artemis:${Version} 528547571480.dkr.ecr.us-east-1.amazonaws.com/artemis:${Version}
            ''' 
        } 
    } 
}
    stage("Push Image"){ 
      timestamps { 
       	ws { 
        sh ''' 
          docker push 528547571480.dkr.ecr.us-east-1.amazonaws.com/artemis:${Version} 
        ''' 
    }
  } 
} 
    stage("Send slack notifications"){ 
      timestamps { 
        ws { 
          echo "Slack" 
         //slackSend color: '#BADA55', message: 'Hello, World!' 
      }
    }
  }
}
