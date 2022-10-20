:- dynamic sampleSize/1.
:- dynamic depByYear/1.
:- dynamic aprecByParkSlot/1.
:- dynamic currYear/1.

:- assertz(currYear(2022)).

:- assertz(sampleSize(3)).

:- assertz(depByYear(0.005)).

:- assertz(aprecByParkSlot(0.025)).

:- assertz(aprecByBathRoom(0.01)).

extra(id,desc,perc).

energyCert(id,perc).

postalCode(pref,sufix).

condition("Normal",0).
condition("New",0.05).
condition("Renovated",0.02).
condition("Old",-0.05).

estate(1,"Apartment","New",117,"T3",1970,"A2",0,2,"Rua X",[4400,130],15000,["Pool","Terrace"]).
estate(2,"Apartment","New",123,"T3",1978,"A2",1,2,"Rua T",[4400,121],15000,[]).
estate(3,"Apartment","New",103,"T3",1974,"A2",3,2,"Rua Y",[4400,130],15000,[]).
estate(4,"Apartment","New",133,"T3",1972,"A2",2,2,"Rua U",[4400,133],15000,[]).
estate(5,"Apartment","New",124,"T3",1979,"A2",1,2,"Rua K",[4400,140],15000,[]).

evaluation(1,1).

/*FALTA FAZER*/
gera_facto:.

evaluateComponent(Value,Desc,FinalValue,Element,Mode):-    
    (Element == "Condition" ->
        condition(Desc,perc),
        (Mode == "Appreciate" ->    
            Multiplier is 1+perc;
            Multiplier is 1-perc);
        energyCert(Desc,perc)        
        (Mode == "Appreciate" ->    
            Multiplier is 1+RoomValue;
            Multiplier is 1-RoomValue)),    
    FinalValue is Multiplier*Value.

evaluateConstYear(Value,ConstYear,FinalValue,Mode):-
    currYear(CurrYear),
    depByYear(DepYear),
    (Mode == "Appreciate" ->
        Multiplier is 1-DepYear;
        Multiplier is 1+DepYear),
    DiffYears is CurrYear-ConstYear,
    potencia(Multiplier,DiffYears,Res),
    FinalValue is Res*Value.

evaluateSpots(Value,Number,FinalValue,Element,Mode):-
    (Element == "Park" ->
        aprecByParkSlot(ParkValue),
        (Mode == "Appreciate" ->    
            Multiplier is 1+ParkValue;
            Multiplier is 1-ParkValue);
        aprecByBathRoom(RoomValue),
        (Mode == "Appreciate" ->    
            Multiplier is 1+RoomValue;
            Multiplier is 1-RoomValue)),    
    potencia(Multiplier,Number,Res),
    FinalValue is Res*Value.

/*FALTA FAZER*/
evaluateExtras(Value,Extras,FinalValue).

potencia(X,1,X).
potencia(Base,Potencia,Res):-
    Potencia2 is Potencia - 1,
    potencia(Base,Potencia2,Res2),
    Res is Res2 * Base.


evaluate(Estate,BaseValue,Mode,FinalValue):-
    estate(Estate,_,Condition,_,_,ConstYear,EnergyCert,ParkSlots,RoomValue,_,_,Value,ListaExtras),
    evaluateConstYear(Value,ConstYear,Value1,Mode),
    evaluateSpots(Value1,ParkSlots,Value2,"Park",Mode),
    evaluateSpots(Value2,RoomValue,Value3,"NBath",Mode),
    evaluateComponent(Value3,Condition,Value4,"Condition",Mode),    
    evaluateComponent(Value4,EnergyCert,Value5,"EnergyCert",Mode),
    evaluateExtras(Value5,ListaExtras,FinalValue,Mode).

get_average(List,Average):-
    sum( List, Sum ),
    length( List, Length ),
    Length > 0, 
    Average is Sum / Length.

sum([],0).
sum([H|L],Sum):-
    sum(L,Sum2),
    Sum is Sum2 + H.
    
box(Box,[Box]). 
   
teste(X,Lista):-member(X,Lista).   

arranca_motor:-
    evaluation(N,Estate),
    get_sample(Estate,Sample).

get_sample(Estate,Sample):-
    estate(Estate,EstateType,_,_,Tipology,_,_,_,_,_,PostalCode,_,_),
    findSamples(EstateType,Tipology,PostalCode,Sample,[],1,1).

incDev2(YDev,YDev2):-(YDev>0 -> YDev2 is YDev+1;YDev2 is YDev-1).
incDev(YDev,Final):-teste(YDev,Temp),Final is Temp*(0-1).

findSamples(_,_,_,Sample,Result,XDev,YDev):- length(Result, Length), Length == 5, Sample is Result.
findSamples(EstateType,Tipology,[Prefix,Sufix],_,Result,_,_):-
    estate(Estate,EstateType,_,_,Tipology,_,_,_,_,_,[Prefix,Sufix],_,_),
    !member(Estate,Result),
    box(Estate,ListA),
    append([Result, ListA], List),
    findSamples(EstateType,Tipology,[Prefix,Sufix],_,List,XDev,YDev).

findSamples(EstateType,Tipology,[Prefix,Sufix],_,Result,XDev,YDev):-
    Sufix2 is Sufix+YDev,
    estate(Estate,EstateType,_,_,Tipology,_,_,_,_,_,[Prefix,Sufix2],_,_),
    !member(Estate,Result),
    box(Estate,ListA),
    append([Result, ListA], List),
    incDev(YDev,YDev2), 
    findSamples(EstateType,Tipology,[Prefix,Sufix],_,List,XDev,YDev2).

findSamples(EstateType,Tipology,[Prefix,Sufix],_,Result,XDev,YDev):-
    Prefix2 is Prefix+XDev,
    estate(Estate,EstateType,_,_,Tipology,_,_,_,_,_,[Prefix2,_],_,_),
    !member(Estate,Result),
    box(Estate,ListA),
    append([Result, ListA], List),
    incDev(XDev,XDev2), 
    findSamples(EstateType,Tipology,[Prefix,Sufix],_,List,XDev2,YDev).