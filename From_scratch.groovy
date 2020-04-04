node {
	properties([
		// Below line sets "Discard Builds more than 5"
		buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '', numToKeepStr: '5')), 
		disableConcurrentBuilds(),
		// Below line triggers this job every minute
		pipelineTriggers([pollSCM('* * * * *')]),
		parameters([
			// Asks for environment to build
			choice(choices: [
			'dev1.olgaojjeh.com', 
			'qa1.olgaojjeh.com', 
			'stage1.olgaojjeh.com', 
			'prod1.olgaojjeh.com'], 
			description: 'Please choose an environment', 
			name: 'ENVIR'),
			// Asks for version 
			choice(choices:  [
				'v0.1', 
				'v0.2', 
				'v0.3', 
				'v0.4', 
				'v0.5'],  
            description: 'Which version should we deploy?',  
            name: 'Version')
		])
	])

		// Pulls a repo from developer
	stage("Pull Repo"){
		git   'https://github.com/farrukh90/cool_website.git'
	}
		//Installs web server on different environment
	stage("Install Prerequisites"){
		sh """
		ssh centos@${ENVIR}                 sudo yum install httpd -y
		"""
	}
		//Copies over developers files to different environment
	stage("Copy artifacts"){
		sh """
		scp -r *  centos@${ENVIR}:/tmp
		ssh centos@${ENVIR}                 sudo cp -r /tmp/index.html /var/www/html/
		ssh centos@${ENVIR}                 sudo cp -r /tmp/style.css /var/www/html/
		ssh centos@${ENVIR}				    sudo chown centos:centos /var/www/html/
		ssh centos@${ENVIR}				    sudo chmod 777 /var/www/html/*
		"""
	}
		//Restarts web server
	stage("Restart web server"){
		sh "ssh centos@${ENVIR}               sudo systemctl restart httpd"
	}

		//Sends a message to slack
	stage("Slack"){
		slackSend color: '#BADA55', message: 'Hello, World!'
	}
}
