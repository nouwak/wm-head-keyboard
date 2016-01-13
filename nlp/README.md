== To start server side app

$> deploy-service (script may require setting correct tomcat directory path)

== Resources

localhost:8080/nlp/probability/get - body {l1: 'A'} - get probabilities of the next letter if the last one is A 

localhost:8080/nlp/probability/add - body {l1: 'A', l2: 'B'} - increase probability of letter B if the last one is A