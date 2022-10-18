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

:-consult('system').

:- http_handler('/real_estate',start_system,[]).

start_server:-
    http_server(http_dispatch, [port(45000)]).
stop_server:-
    http_stop_server(45000, []).

start_system(Request):-
    cors_enable,
    member(method(post), Request), !,
    http_read_json_dict(Request, Input),
    format(user_output,'Request received:~p~n with body:~p~n~n',[Request,Input]),
    (reply_json_dict(Output);
    atom_json_dict('{"message":"Error generating output"}', Error,_),
    reply_json_dict(Error,[status([400])])).







