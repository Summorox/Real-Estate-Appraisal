:-op(220,xfx,then).
:-op(35,xfy,if).
:-op(240,fx,rule).
:-op(600,xfy,and).

:-consult('rules').

:-dynamic justification/3.

% Load knowledge Base

load_kb:-
		read(NBC),
		consult('rules').
		
% Launch inference engine

launch_engine:-fact(N,fact),
		rule ID if LHS then RHS,
		fact_triggers_rule(N,fact,ID,LHS,RHS),
		latest_fact(N),latest_rule(ID).


% Verificar se o LHS da regra tem sucesso

fact_triggers_rule(N,fact,ID,LHS,RHS):-
	fact_is_in_condition(fact,LHS),
	verify_conditions(LHS,LFacts),
	member(N,LFacts),
	conclude(RHS,ID,LFacts),
	!.

fact_triggers_rule(_,_,_,_,_).


fact_is_in_condition(F,[F  e _]).

fact_is_in_condition(F,[evaluate(F1)  e _]):- F=..[X,X1|_],F1=..[X,X1|_].

fact_is_in_condition(F,[_ e Fs]):-
	fact_is_in_condition(F,[Fs]).

fact_is_in_condition(F,[F]).

fact_is_in_condition(F,[evaluate(F1)]):-F=..[X,X1|_],F1=..[X,X1|_].



verify_conditions([X e Y],[N|LF]):-
	fact(N,X),!,
	verify_conditions([Y],LF).

verify_conditions([evaluate(X) e Y],[N|LF]):-
	evaluate(N,X),!,
	verify_conditions([Y],LF).

verify_conditions([X],[N]):- fact(N,X),!.

verify_conditions([evaluate(X)],[N]):- evaluate(N,X).


evaluate(N,P):-	P=..[Functor,Entidade,Operando,Valor],
		P1=..[Functor,Entidade,Valor1],
		fact(N,P1),
		compare(Valor1,Operando,Valor).

compare(V1,==,V):- V1==V.
compare(V1,\==,V):- V1\==V.
compare(V1,>,V):-V1>V.
compare(V1,<,V):-V1<V.
compare(V1,>=,V):-V1>=V.
compare(V1,=<,V):-V1=<V.


% Aplicar o RHS da regra que foi disparada com sucesso

conclude([create_fact(F)|Y],ID,LFacts):-
	!,
	create_fact(F,ID,LFacts),
	conclude(Y,ID,LFacts).

conclude([],_,_):-!.



create_fact(F,_,_):-
	fact(_,F),!.

create_fact(F,ID,LFacts):-
	retract(latest_fact(N1)),
	N is N1+1,
	asserta(latest_fact(N)),
	assertz(justificationtion(N,ID,LFacts)),
	assertz(fact(N,F)),
	write('Fact nº was concluded'),write(N),write(' -> '),write(F),get0(_),!.



% Visualização da base de factos

show_facts:-
	findall(N, fact(N, _), LFacts),
	write_facts(LFacts).

write_facts([I|R]):-fact(I,F),
	write('The fact nº '),write(I),write(' -> '),write(F),nl,
	write_facts(R).
write_facts([]).








