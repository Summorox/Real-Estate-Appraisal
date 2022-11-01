:- dynamic appreciation/3.
:- dynamic flag/1.
:- dynamic deal/6.
:- dynamic estate/14.
:- dynamic postalCodeList/2.
:- consult('KB').

teste(S):- format("testando ~w a ver. ~n nao da", S).
flag("decrementa").
postalCodeList(4400,[130,121,133,140]).


/*predicado que carrega base conhecimento*/
carrega_bc:-
    write('NOME DA BASE DE CONHECIMENTO (terminar com .)-> '),
% usar se necessario caminho absoluto com / e colocar entre plicas
        read(NBC),
        consult(NBC).

nothing(_).

/*predicado que imprime os factos para cada imovel avaliado*/
getData(Estate,List,String,It1,Final):-
    appreciation(Estate,Element,Value),
    not(member(Element,List)),
    (It1 == 0 -> String = '';nothing(0)), 
    (Value == 0.0 -> 
    box(Element,Box),
    append([Box,List],NewList),
    getData(Estate,NewList,String,1,Final);
    Temp1 is round(Value),
    (Value>0 -> 
    swritef(First,' because of the %w the estate value increased by %w; ',[Element,Temp1]);
    swritef(First,' because of the %w the estate value decreased by %w; ',[Element,Temp1])),
    atom_concat(String,First,String2),
    box(Element,Box),
    append([Box,List],NewList),
    getData(Estate,NewList,String2,1,Final)).
getData(_,_,Final,_,Final).

why_not(Estate,Quality,String):-
    deal(Estate,_,_,_,_,Quality),
    swritef(String,'The real estate business is already rated as %w!',[Quality]).
why_not(Estate,Quality,String):-
    deal(Estate,_,_,_,Perc,Quality2),
    businessQuality([Lower,Upper],Quality),
    Temp is (Perc-1),Temp1 is abs(Temp)*100,
    Temp2 is round(Temp1),
    swritef(First,'The real estate business was rated as %w ,because the real estate estimate value is %w',[Quality2,Temp2]),
    (Temp>0 -> 
    swritef(Second,'% above the client required value.For it to be rate as a %w business, the estimated value should be between ',[Quality]);
    swritef(Second,'% below the client required value.For it to be rate as a %w business, the estimated value should be between ',[Quality])),
    Temp3 is (Lower-1),
    atom_concat(First,Second,Third),
    (Temp3>=0 -> 
    Temp4 is abs(Temp3)*100,Temp5 is round(Temp4),
    Temp6 is (Upper-1),Temp7 is abs(Temp6)*100, Temp8 is round(Temp7),
    swritef(Fourth,'%w% and %w% above the client required value',[Temp5,Temp8]);
    Temp4 is ((1-Lower)*100),Temp5 is ((1-Upper)*100),
    Temp6 is round(Temp4),Temp7 is round(Temp5),
    swritef(Fourth,'%w% and %w% below the client required value',[Temp7,Temp6])),
    atom_concat(Third,Fourth,String).
why_not(_,_,String):-
    String = 'That estate doesnt exist or was not evaluated.'.    

/*predicado de explicações do porque*/
how(Estate,FinalString):-
    deal(Estate,_,_,0,_,_),
    swritef(FinalString,'The real estate n%w was already known!',[Estate]).
how(Estate,FinalString):-
    deal(Estate,EstimatedValue,ClientValue,BaseValue,_,Quality),
    Temp1 is round(EstimatedValue),
    Temp2 is round(BaseValue),
    getData(Estate,[],_,0,Final),
    swritef(FinalString,'The real estate number %w was estimated in %w , because based on the started value %w we applied the following rules: %w. According to the value required by the client (%w) and the estimated value calculated before, we rate this deal as %w!',[Estate,Temp1,Temp2,Final,ClientValue,Quality]).
how(Estate,FinalString):-
    FinalString = 'That estate doesnt exist or was not evaluated.'.    


/*metodo que vai buscar o multiplicador para cada avaliação*/
getMultiplier(Mode,Perc,Multiplier):-
    (Mode == "Appreciate" ->    
            Multiplier is 1+Perc;
            Multiplier is 1-Perc).

/*metodo que vai verificar se é para gerar factos ou não*/
generate_facts(Estate,Element,Value,FinalValue,"Appreciate"):-
    Difference is FinalValue-Value,
    generate_facts2(Estate,Element,Difference).
generate_facts(_,_,_,_,_).

/*vai criar factos para um determinado imovel*/
generate_facts2(Estate,Element,Difference):-
    assertz(appreciation(Estate,Element,Difference)).

/*metodo que vai avaliar um imovel pela sua condição e certificado energetico*/
evaluateComponent(Estate,Value,Desc,FinalValue,Element,Mode):-    
    (Element == "house condition" ->
        condition(Desc,Perc);
        energyCert(Desc,Perc)),        
    getMultiplier(Mode,Perc,Multiplier),            
    FinalValue is Multiplier*Value,
    generate_facts(Estate,Element,Value,FinalValue,Mode).
    
/*metodo que vai avaliar um imovel pelo seu ano de construção*/
evaluateConstYear(Estate,Value,ConstYear,FinalValue,Mode):-
    currYear(CurrYear),
    rules("years depreciation",DepYear),
    getMultiplier(Mode,DepYear,Multiplier),
    DiffYears is CurrYear-ConstYear,
    potencia(Multiplier,DiffYears,Res),
    FinalValue is Res*Value,
    generate_facts(Estate,"years depreciation",Value,FinalValue,Mode).

/*metodo que vai avaliar um imovel pelo seu numero de quartos de banho e lugares de estacionamento*/
evaluateSpots(Estate,Value,Number,FinalValue,Element,Mode):-
    rules(Element,Perc),
    getMultiplier(Mode,Perc,Multiplier),
    potencia(Multiplier,Number,Res),
    FinalValue is Res*Value,
    generate_facts(Estate,Element,Value,FinalValue,Mode).

/*avaliação da lista de extras de um imovel*/
evaluateExtras(_,X,[],X,_).
evaluateExtras(Estate,Value,[T|L],FinalValue2,Mode):-
    evaluateExtras(Estate,Value,L,FinalValue,Mode),
    rules(T,Perc),
    getMultiplier(Mode,Perc,Multiplier),
    FinalValue2 is FinalValue*Multiplier,
    generate_facts(Estate,T,FinalValue,FinalValue2,Mode).

/*vai calcular uma potencia*/
potencia(_,0,1).
potencia(X,1,X).
potencia(Base,Potencia,Res):-
    Potencia2 is Potencia - 1,
    potencia(Base,Potencia2,Res2),
    Res is Res2 * Base.

/*vai avaliar o imovel segundo diversos criterios*/
evaluate(Estate,BaseValue,FinalValue,Mode):-
    estate(Estate,_,Condition,_,_,ConstYear,EnergyCert,ParkSlots,BathRooms,_,_,_,ListaExtras,_),
    evaluateConstYear(Estate,BaseValue,ConstYear,Value1,Mode),
    evaluateSpots(Estate,Value1,ParkSlots,Value2,"number of parking slots",Mode),
    evaluateSpots(Estate,Value2,BathRooms,Value3,"number of bathrooms",Mode),
    evaluateComponent(Estate,Value3,Condition,Value4,"house condition",Mode),    
    evaluateComponent(Estate,Value4,EnergyCert,Value5,"energy certfication",Mode),
    evaluateExtras(Estate,Value5,ListaExtras,FinalValue,Mode).

/*vai calcular a media dos valores apos depreciação dos imoveis*/
get_average(List,Average):-
    takeOutFirst(List,NewList),
    getDepreciatedValues(NewList,ValueList),
    sum( ValueList, Sum ),
    length( ValueList, Length ),
    Length > 0, 
    Average is Sum / Length.

/*vai receber uma lista com os ids de imoveis e retornar o uma lista com os seus valores depreciados*/
getDepreciatedValues([],[]).
getDepreciatedValues([T|L],[H|R]):-
    getDepreciatedValues(L,R),
    depreciate(T,H).

/*metodo de depreciação de um imovel*/
depreciate(Estate,Value):-
    deal(Estate,EvaluationValue,_,_,_,_),
    evaluate(Estate,EvaluationValue,Value,"Depreciate").

/*soma todos os elementos de uma lista*/
sum([],0).
sum([H|L],Sum):-
    sum(L,Sum2),
    Sum is Sum2 + H.

/*mete um objecto dentro de uma lista*/
box(Box,[Box]). 

/*qualifica o negocio*/
qualify_deal(ClientValue,FinalValue,Perc,Quality):-
    businessQuality([Min,Max],Quality),
    Perc is FinalValue/ClientValue,
    Perc > Min,
    Perc < Max.   

/*altera o estado de um imovel para evaluated*/
changeState(Estate):-
    retract(estate(Estate,V1,V2,V3,V4,V5,V6,V7,V8,V9,V10,V11,V12,_)),
    asserta(estate(Estate,V1,V2,V3,V4,V5,V6,V7,V8,V9,V10,V11,V12,"Evaluated")).

/*vai fazer o reset á flag, que é um predicado usado durante o processo principal*/
resetFlag:-
    retract(flag(_)),
    asserta(flag("decrementa")).

/*adiciona o codigo Postal ao grupo do respetivo prefixo*/
addPostal(Prefixo,Sufixo):-
    postalCodeList(Prefixo,List),
    not(member(Sufixo,List)),
    box(Sufixo,Box),
    append([Box,List],NList),
    sort(NList,NewList),
    retract(postalCodeList(Prefixo,List)),
    assertz(postalCodeList(Prefixo,NewList)).
addPostal(_,_).

/*predicado principal*/
arranca_motor:-
    arranca_motor2([]).    
    
evaluateSingular(Estate,FinalValue,Quality):-
    deal(Estate,FinalValue,_,_,_,Quality).

evaluateSingular(Estate,FinalValue,Quality):-
    estate(Estate,_,_,_,_,_,_,_,_,_,[Prefixo,Sufixo],ClientValue,_,_),
    addPostal(Prefixo,Sufixo),
    get_sample(Estate,Sample),
    get_average(Sample,Average),
    evaluate(Estate,Average,FinalValue,"Appreciate"),
    qualify_deal(ClientValue,FinalValue,Perc,Quality),
    assertz(deal(Estate,FinalValue,ClientValue,Average,Perc,Quality)),
    changeState(Estate),
    resetFlag.

/*predicado que corre para avaliar cada imovel que ainda esta por avaliar*/
arranca_motor2(List):-
    estate(Estate,_,_,_,_,_,_,_,_,_,[Prefixo,Sufixo],ClientValue,_,"Not_Evaluated"),
    not(member(Estate,List)),
    addPostal(Prefixo,Sufixo),
    get_sample(Estate,Sample),
    get_average(Sample,Average),
    evaluate(Estate,Average,FinalValue,"Appreciate"),
    qualify_deal(ClientValue,FinalValue,Perc,Quality),
    assertz(deal(Estate,FinalValue,ClientValue,Average,Perc,Quality)),
    changeState(Estate),
    resetFlag,
    box(Estate,ListBox),
    append([ListBox,List],NewList),
    arranca_motor2(NewList).
/*metodo de paragem deo arranca_motor. vai correr quando nao existirem mais imoveis para avaliar*/
arranca_motor2(_).

/*predicado auxiliar*/
copy(X,X).

/*predicado auxiliar que retorna uma lista sem o seu primeiro elemento*/
takeOutFirst([_|L],L).

/*passado um determinado id de um imovel, vai retornar uma amostra com as mesmas caracteristicas*/
get_sample(Estate,Sample):-
    estate(Estate,EstateType,_,_,Tipology,_,_,_,_,_,[Prefix,Sufix],_,_,_),
    postalCodeList(Prefix,List),
    pos(Sufix,List,1,Position),
    box(Estate,BoxList),
    findSamples(EstateType,Tipology,[Prefix,Sufix],Sample,BoxList,1,1,List,Position).

/*retorna o index de um determinado elemento numa lista*/
pos(_,[],N,N).
pos(X,[X|_],N,N).
pos(X,[_|L],Pos,N):-
    Pos1 is Pos+1,
    pos(X,L,Pos1,N).

/*predicado que vai buscar o elemento numa dada posição*/
getElementAtPos(0,_,[],_).
getElementAtPos(Element,Pos,[Element|_],Pos).
getElementAtPos(Element,Pos,[_|L],N):-
    N1 is N+1,
    getElementAtPos(Element,Pos,L,N1).    

/*predicado auxiliar para pesquisar a lista de sufixos de cada prefixo*/
incDev(YDev,YDev2):-
    flag(Mode),
    (Mode=="incrementa" ->
    (YDev<0 -> 
    (Temp is 0-YDev,
    YDev2 is Temp+1); 
    YDev2 is YDev+1),
    NewMode = "decrementa";
    YDev2 is 0-YDev,
    NewMode = "incrementa"),
    retract(flag(Mode)),
    asserta(flag(NewMode)).

/*Criterio de paragem, ja existe uma amostra razoavel*/
findSamples(_,_,_,Result,Result,_,_,_,_):- length(Result, Length), sampleSize(Size), Temp is Size +1, Length == Temp.
/*No caso de existirem imoveis no mesmo prefixo e sufixo*/
findSamples(EstateType,Tipology,[Prefix,Sufix],Sample,Result,XDev,YDev,List,Position):-
    estate(Estate,EstateType,_,_,Tipology,_,_,_,_,_,[Prefix,Sufix],_,_,_),
    not(member(Estate,Result)),
    box(Estate,ListA),
    append([Result, ListA], FinalList),
    findSamples(EstateType,Tipology,[Prefix,Sufix],Sample,FinalList,XDev,YDev,List,Position).
/*No caso de nao existerem imoveis no mesmo sufixo*/
findSamples(EstateType,Tipology,[Prefix,Sufix],Sample,Result,XDev,YDev,List,Position):-
    length( List, Length ), 
    Temp is Position+YDev,
    ((Temp < 1 ; Temp > Length) ->
    incDev(YDev,YDev2), 
    findSamples(EstateType,Tipology,[Prefix,Sufix],Sample,Result,XDev,YDev2,List,Position);
    getElementAtPos(Sufix2,Temp,List,1),
    estate(Estate,EstateType,_,_,Tipology,_,_,_,_,_,[Prefix,Sufix2],_,_,_),
    not(member(Estate,Result)),
    box(Estate,ListA),
    append([Result, ListA], FinalList),
    incDev(YDev,YDev2), 
    findSamples(EstateType,Tipology,[Prefix,Sufix],Sample,FinalList,XDev,YDev2,List,Position)).
/*No caso de nao existerem imoveis no mesmo prefixo*/
findSamples(EstateType,Tipology,[Prefix,Sufix],_,Result,XDev,YDev,List,Pos):-
    Prefix2 is Prefix+XDev,
    estate(Estate,EstateType,_,_,Tipology,_,_,_,_,_,_,[Prefix2,_],_,_),
    not(member(Estate,Result)),
    box(Estate,ListA),
    append([Result, ListA], FinalList),
    incDev(XDev,XDev2), 
    findSamples(EstateType,Tipology,[Prefix,Sufix],_,FinalList,XDev2,YDev,List,Pos).
