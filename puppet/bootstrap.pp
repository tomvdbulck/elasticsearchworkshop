Exec { path => [ "/bin/", "/sbin/" , "/usr/bin/", "/usr/sbin/" ] }

########### Repository management ###########
class { 'apt':
  apt_update_frequency => weekly
}

################### Java ####################
class { 'java':
  distribution => 'jre',	# defaults to jdk
}

############### Elasticsearch ###############
class { 'elasticsearch':
  version => '1.1.1',
  manage_repo  => true,
  repo_version => '1.1',	# corresponds with the major version of Elasticsearch
  config => { 
  	'cluster.name' => 'ordina-dev-001',
    'network.host' => '0.0.0.0',
  }
}

elasticsearch::instance { 'ord-01':
  config => { 'node.name' => 'dataNode-01' }
}
elasticsearch::instance { 'ord-02':
  config => { 'node.name' => 'dataNode-02' }
}

elasticsearch::plugin{'elasticsearch/marvel/latest':
  module_dir => 'marvel',
  instances  => 'ord-01'
}
