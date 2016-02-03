package io.github.jdocker.agent;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

public class DockerAgentConfiguration extends Configuration {

        private String agentName;

        @JsonProperty
        public String getAgentName() {
            return agentName;
        }

        @JsonProperty
        public void setAgentName(String agentName) {
            this.agentName = agentName;
        }

}