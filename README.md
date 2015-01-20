# elasticsearchworkshop
Basic Workshop ElasticSearch


# Requirements #

1. make sure the following items have been installed on your machine:
   - java 7 or 8
   - git
   - maven
   
2. Install Oracle VirtualBox
  https://www.virtualbox.org/wiki/Downloads

3. Install Vagrant
   https://www.vagrantup.com/downloads.html
  (on Mac and Windows the installer will make sure that vagrant command is known in the command prompt)

4. Clone this repository in your workspace

5. Open a command prompt and go to the elasicworkshop folder and type "vagrant up"
   This will start up the vagrant box, the first time will take a while as it has to download the OS, elasticsearch, ...
   Shutting down the vagrant box - can be done via "vagrant halt".

6. Import the maven projects into your IDE

7. Run the tests of wes-core to verify everything has been setup correctly 

You are ready to go.

PS
If you like a pretty shiny interface to deal with git, you can install sourcetree: http://www.sourcetreeapp.com/
