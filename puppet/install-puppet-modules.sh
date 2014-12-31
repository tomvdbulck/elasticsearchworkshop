#!/bin/bash
mkdir -p /etc/puppet/modules;

if [ ! -d /etc/puppet/modules/apt ]; then
	puppet module install puppetlabs-apt
fi

if [ ! -d /etc/puppet/modules/java ]; then
	puppet module install puppetlabs-java
fi

if [ ! -d /etc/puppet/modules/elasticsearch ]; then
	puppet module install elasticsearch-elasticsearch
fi
