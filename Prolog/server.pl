:- use_module(library(http/thread_httpd)).
:- use_module(library(http/http_dispatch)).
:- use_module(library(http/http_open)).
:- use_module(library(http/json)).
:- use_module(library(http/http_ssl_plugin)).
:- use_module(library(http/http_json)).
:- use_module(library(http/http_cors)).
:- use_module(library(settings)).
:- use_module(library(http/json_convert)).
:- set_setting(http:cors,[*]).
:- use_module(library(term_to_json)).
:-consult('predicadosRun').


:- http_handler('/real_estate_prolog',start_system,[]).

start_server:-
    http_server(http_dispatch, [port(45000)]).
stop_server:-
    http_stop_server(45000, []).

start_system(Request):-
    cors_enable,
    member(method(post), Request), !,
    http_read_json_dict(Request, Input),
    format(user_output,'Request received:~p~n with body:~p~n~n',[Request,Input]),
    (Input._ == "evaluate" ->(
        load_estate(Input),
        evaluateSingular(Input.id,_,_),
        generate_output_evaluate(Output,Input.id) -> !,
        reply_json_dict(Output);
        atom_json_dict('{"message":"Error generating output"}', Error,_),
        reply_json_dict(Error,[status([400])]));
        ((Input._ == "how" ->
            how(Input.id,String);
            why_not(Input.id,Input.question,String)),
            Output = json{reason:String},
            reply_json_dict(Output);
            atom_json_dict('{"message":"Error generating output"}', Error,_),
            reply_json_dict(Error,[status([400])])
        )    
    ).


load_estate(Estate):-
    retractall(estate(Estate.id,_,_,_,_,_,_,_,_,_,_,_,_,_)),
    assert(estate(Estate.id,Estate.type,Estate.condition,Estate.m2,Estate.typology,Estate.year,Estate.certificate,Estate.parkSlots,Estate.bathrooms,Estate.address,Estate.zipcode,Estate.clientPrice,Estate.items,"Not_Evaluated")).

generate_output_evaluate(Output,EstateId):-
	generate_output_deal_evaluate(EstateId,OutputDeal),
        Output = json{deal:OutputDeal}.


generate_output_deal_evaluate(EstateId,OutputDeal):-
    findall(json{id:EstateId,clientPrice:ClientPriceString,evaluationPrice:EvaluationPriceString,perc:PercString,
    quality:QualityString},
    (deal(EstateId,EvaluationPrice,ClientPrice,_,Perc,Quality),
    atom_string(ClientPrice,ClientPriceString),
    atom_string(EvaluationPrice,EvaluationPriceString),
    atom_string(Perc,PercString),
    atom_string(Quality,QualityString)),
    OutputDeal).









