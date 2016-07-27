
motorway(53,62).
motorway(62,83).
motorway(62,63).
motorway(62,64).
motorway(62,76).
motorway(62,66).
motorway(53,68).
motorway(54,56).
motorway(54,62).
motorway(54,68).
motorway(54,83).
motorway(63,66).
motorway(83,62).
motorway(60,83).
motorway(60,68).
motorway(60,83).
motorway(63,64).
motorway(63,76).
motorway(63,64).
motorway(64,69).
motorway(68,66).
motorway(69,66).
footpath(52,53).
footpath(53,54).
footpath(56,58).
footpath(56,57).
footpath(57,58).
footpath(57,59).
footpath(58,59).
footpath(59,60).
footpath(65,68).
footpath(76,69).
footpath(83,65).
footpath(68,65).
footpath(52,-1).
footpath(57,-3).
footpath(A,B):-footpath(B,A)
motorway(A,B):-motorway(B,A)
highwayRequired(A,B) :- footpath(A,_),footpath(B,_),not(footpath(A,B)),not(motorway(A,B)).
directRoute(A,B) :- footpath(A,B);motorway(A,B);footpath(B,A);motorway(B,A).
route(A,B) :-   
  walk(A,B,[]);walk(B,A,[]).            

walk(A,B,V) :-       
  footpath(A,X);motorway(A,X) ,       
  not(member(X,V)) , 
  (                  
    B = X;walk(X,B,[A|V])  
  ).   


