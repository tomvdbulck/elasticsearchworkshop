# Elasticsearch workshop
Basic Workshop ElasticSearch


# Installation & Setup #

1. Make sure the following items have been installed on your machine:
   - Java 7 or 8
   - Git
   - Maven
   
2. Install Oracle VirtualBox
  https://www.virtualbox.org/wiki/Downloads

3. Install Vagrant
   https://www.vagrantup.com/downloads.html
  (on Mac and Windows the installer will make sure that vagrant command is known in the command prompt)

4. Clone this repository in your workspace

5. Open a command prompt and go to the elasicworkshop folder and type "vagrant up"
   This will start up the vagrant box, the first time will take a while as it has to download the OS, elasticsearch, ...
   Shutting down the vagrant box - can be done via "vagrant halt".
   with "vagrant provision" you can restore it back to a clean, working state

6. Import the maven projects into your IDE

7a. Run the tests of wes-core to verify everything has been setup correctly 

7b. You can also access the elastic HQ console on http://localhost:9200/_plugin/HQ (open source and free)

7c. Or the marvel plugin on http://localhost:9200/_plugin/marvel/kibana/index.html (official and only free for development)


For IntelliJ users: set the working directory in the running configurations to  $MODULE_DIR$



You are ready to go.

PS
If you like a pretty shiny interface to deal with git, you can install SourceTree: http://www.sourcetreeapp.com/

#### Note
If you are using Windows, you may need to enable hardware virtualization (VT-x). It can usually be enabled via your BIOS.
