:- dynamic sampleSize/1.
:- dynamic depreciationByYear/1.
:- dynamic aprecByParkSlot/1.

/vai definir o tamanho da amostra/
:- assertz(sampleSize(5)).

//vai definir o valor de depreciação por cada ano apos a construção//
:- assertz(depByYear(0.03)).

//vai definir o valor de apreciação por cada lugar de estacionamento//
:- assertz(aprecByParkSlot(0.025)).

//vai definir o valor de apreciação por cada casa de banho//
:- assertz(aprecByBathRoom(0.01)).

/id - id do extra
 desc - descrição
 perc - percentagem de apreciação/
extra(id,desc,perc).

/id - id do tipo de certificado energetico
 perc - percentagem de apreciação/
energyCert(id,perc)

/pref - prefixo,
 sufix - sufixo/
postalCode(pref,sufix).

/id - id do estate
 EstateType - tipo de imovel
 Condition - condição do imóvel
 GrossArea
 Tipology - tipologia do imovel
 ConstYear - ano de construção do imovel
 EnergyCert - certificado energetico
 ParkSlots - numero de lugares de estacionamento
 NBathrooms - numero de casas de banho
 Address - endereço
 PostalCode - codigo postal
 ClientValue - valor inserido pelo cliente para o imovel
 Extras - Lista de extras do imovel/
estate(Id,EstateType,Condition,GrossArea,Tipology,ConstYear,EnergyCert,ParkSlots,NBathrooms,Address,PostalCode,ClientValue,Extras)

arranca_motor:-
    evaluation(N,Estate),
    get_sample(Estate,Sample),
    calculate_regression(Sample,Values),
    get_average(Values,Average),
    applicate_rules(Estate,Average,FinalValue),
    get_clientValue(Estate,ClientValue).,
    qualify_deal(FinalValue,ClientValue).
    last_estate(N).

//vai procurar uma quantidade de elementos (avaliar o predicado sampleSize em cima) para a amostra, o mais proximo possivel do nosso imovel
criterios para selecionar area : codigo postal, o tipo de imovel e tipologia//
get_sample(Estate,Sample).

//vai receber uma lista de imoveis que constituem a amostra e a partir dai chamar o predicado "regression". O predicado "regression" vai retornar o valor 
base do imovel, e guardamos esse valor numa lista que retornamos no fim//
calculate_regression(Sample,Values).

//este predicado vai receber um imovel e chamar o predicado "evaluate" em modo de depreciação. No fim, o predicado "evaluate" retorna um valor que 
retornamos ao processo anterior//
regression(Estate,BaseValue).

//vai consultar os extras, e adicionar ou remover valor consoante a taxa de apreciação do extra//
evaluate(estate,BaseValue, "appreciate").
evaluate(estate,BaseValue, "depreciate").

/com a lista dos valores bases da amostra vai calcular uma media/
get_average(List,Average):-
    sum( List, Sum ),
    length( List, Length ),
    Length > 0, 
    Average is Sum / Length.

//vai chamar o predicado "evaluate", mas desta vez em modo de Apreciação//
applicate_rules(Estate,Average,FinalValue).

//vai ao objecto Casa, que é o objeto que estamos a avaliar e retornamos o valor que o cliente esta a pedir//
get_clientValue(Estate,ClientValue).

//vamos qualificar o negocio em relação ao valor de avaliação, e que o cliente está a pedir//
qualify_deal(FinalValue,ClientValue).