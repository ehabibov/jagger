#!/bin/bash
echo $JAVA_HOME/bin/java -Xmx2550m -Xms2550m $2 $3 $4 $5 $6 $7 $8 $9 -classpath "./modules/chassis/*:./modules/diagnostics/*:./lib/*:./configuration/boot/" -Dlog4j.configuration=jagger.log4j.properties com.griddynamics.jagger.JaggerLauncher $1
$JAVA_HOME/bin/java -Xmx2550m -Xms2550m $2 $3 $4 $5 $6 $7 $8 $9 -classpath "./modules/chassis/*:./modules/diagnostics/*:./lib/*:./configuration/boot/" -Dlog4j.configuration=jagger.log4j.properties com.griddynamics.jagger.JaggerLauncher $1