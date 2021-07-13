# Currently JQAssistant and Gradle 7 do not work together easily. Use the CLI distribution
jqassistant "$@" -f build/quarkus-app/app -storeUri file:build/jqassistant -r jqassistant -reportDirectory build/jqassistant/report
