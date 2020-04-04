node { 

properties([
  // Discards more than 5 builds
  buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '', numToKeepStr: '5')), 
  disableConcurrentBuilds(),
  
  // Triggers this job every minute
  pipelineTriggers([pollSCM('* * * * *')])
  ])






  stage("Pull Repo"){ 
    git "https://github.com/farrukh90/cool_website.git" 
} 
  stage("Install Prerequisites"){ 
    sh """
    sudo yum install httpd -y
    sudo cp -r * /var/www/html/
    sudo systemctl start httpd
    """
} 
  stage("Stage3"){ 
    echo "hello" 
} 
  stage("Stage4"){ 
    echo "hello" 
} 
  stage("Stage5"){ 
    echo "hello" 
  } 
} 
