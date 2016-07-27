wife('Queen Elizabeth 11','Philip').
wife('Diana','Charles').
wife('Camilla','Charles').
wife('Sophie','Edward').
wife('Anne','Captain Mark Phillips').
wife('Anne','Vice-Admiral Timothy Laurence').
wife('Sarah','Andrew').
wife('Kate','William').
wife('Autumn Phillips','Peter Phillips').
wife('Zara Phillips','Mike Tindall').

son('Charles','Philip').
son('Andrew','Philip').
son('Edward','Philip').
son('Charles','Queen Elizabeth 11').
son('Andrew','Queen Elizabeth 11').
son('Edward','Queen Elizabeth 11').

son('William','Charles').
son('Harry','Charles').
son('William','Diana').
son('Harry','Diana').

son('Peter Phillips','Captain Mark Phillips').
son('Peter Phillips','Anne').
son('Edward','James').
son('George','William').
son('George','Kate').
son('James','Edward').
son('James','Sophie').

daughter('Anne','Philip').
daughter('Anne','Queen Elizabeth 11').
daughter('Zara Phillips','Anne').
daughter('Zara Phillips','Captain Mark Phillips').

daughter('Beatrice','Andrew').
daughter('Andrew','Eugenie').
daughter('Edward','Lousie').
daughter('Peter','Savannah').
daughter('Peter','Isla').
daughter('Mike','Mia Grace').
daughter('Anne','Zara').
daughter('Beatrice','Andrew').
daughter('Beatrice','Sarah').
daughter('Eugenie','Andrew').
daughter('Eugenie','Sarah').
daughter('Lousie','Edward').
daughter('Lousie','Sophie').

daughter('Peter Phillips','Savannah').
daughter('Autumn Phillips','Savannah').
daughter('Peter Phillips','Isla').
daughter('Autumn Phillips','Isla').

daughter('Mike Tindall','Mia Grace').
daughter('Zara Phillips','Mia Grace').

child(X,Y):- son(X,Y);daughter(X,Y).
spouse(X,Y):- child(Z,X),child(Z,Y).
husband(X,Y):- wife(Y,X).
parent(X,Y):- son(Y,X);daughter(Y,X).
grandParent(X,Y):-parent(X,Z),parent(Z,Y).
grandChild(X,Y):- child(X,Z),child(Z,Y).
greatGrandChild(X,Y):- child(X,Z),grandChild(Z,Y).
greatGrandParent(X,Y):- parent(X,Z),grandParent(Z,Y).
brother(X,Y):-son(X,Z),son(Y,Z);daughter(Y,Z),son(X,Z).
sister(X,Y):-daughter(X,Z),daughter(Y,Z);son(Y,Z),daughter(X,Z).
halfSister(X,Y):- parent(Z,Y),spouse(Q,Z),not(parent(Q,Y)),daughter(X,Q).
aunt(X,Y):- parent(Z,Y),sister(X,Z); parent(Z,Y),sisterInLaw(X,Z);halfSister(X,Y).
halfBrother(X,Y):- parent(Z,Y),spouse(Q,Z),not(parent(Q,Y)),son(X,Q).
uncle(X,Y):-parent(Z,Y),brother(X,Z); parent(Z,Y),brotherInLaw(X,Z);halfBrother(X,Y).
brotherInLaw(X,Y):- brother(X,Z),spouse(Z,Y);sister(N,Y),spouse(X,N).
sisterInLaw(X,Y):-sister(X,Z),spouse(Z,Y);brother(N,Y),spouse(X,N).


