#!/usr/bin/env python
from grammar_helpers import *

prelude()

rule('Statement', OneOf(
	'InsertStatement', 'SelectStatement',
	'DropTableStatement',
	'SaveStatement', 'LoadStatement', 'CreateDatabaseStatement', 'DropDatabaseStatement',
	'QuitStatement'))

rule('InsertStatement', Sequence(
	'InsertKeyword', Definite(),
	'IntoKeyword',
	('Name', 'table-name'),
	('MaybeInsertColumnList', 'columns'),
	'ValuesKeyword',
	('InsertRowList', 'rows'),
	'Semicolon'))
rule('MaybeInsertColumnList', OneOf('ParenthesizedInsertColumnList', 'Epsilon'))
rule('ParenthesizedInsertColumnList', Sequence('OpenParen', ('ColumnList', 'columns'), 'CloseParen'))
rule('ColumnList', OneOf(
	Sequence(('Name', 'this'), 'Comma', ('ColumnList', 'next')),
	'Name'))
rule('InsertRowList', OneOf(
	Sequence(('ParenthesizedInsertRow', 'this'), 'Comma', ('InsertRowList', 'next')),
	'ParenthesizedInsertRow'))
rule('ParenthesizedInsertRow', Sequence('OpenParen', ('InsertRow', 'values'), 'CloseParen'))
rule('InsertRow', OneOf(
	Sequence(('Expression', 'this'), 'Comma', ('InsertRow', 'next')),
	'Expression'))

rule('SelectStatement', Sequence(
	'SelectKeyword', Definite(),
	('MaybeColumnSet', 'columns'),
	'FromKeyword',
	('Name', 'table-name'),
	'Semicolon'))
rule('MaybeColumnSet', OneOf('ColumnList', 'Asterisk'))

rule('SaveStatement', Sequence(
	'SaveOrCommitKeyword', Definite(),
	'Semicolon'))
rule('SaveOrCommitKeyword', OneOf('SaveKeyword', 'CommitKeyword'))

rule('LoadStatement', Sequence(
	'LoadKeyword', Definite(),
	('Name', 'dbname'),
	'Semicolon'))

rule('CreateDatabaseStatement', Sequence(
	'CreateKeyword', 'DatabaseKeyword', Definite(),
	('Name', 'dbname'),
	'Semicolon'))

rule('DropDatabaseStatement', Sequence(
	'DropKeyword', 'DatabaseKeyword', Definite(),
	('Name', 'dbname'),
	'Semicolon'))

rule('DropTableStatement', Sequence(
	'DropKeyword', 'TableKeyword', Definite(),
	('Name', 'tablename'),
	'Semicolon'))

rule('QuitStatement', Sequence(
	'QuitKeyword', Definite(),
	'Semicolon'))

rule('Expression', OneOf('String', 'Integer', 'Float', 'Name'))

for kw in ("INSERT", "SELECT", "UPDATE", "DROP", "DELETE", "CREATE", "INTO", "FROM", "VALUES", "TABLE", "SAVE", "COMMIT", "LOAD", "DATABASE", "QUIT"):
	rule(kw.title() + 'Keyword', Keyword(kw))
for terminal in ('Semicolon', 'Comma', 'OpenParen', 'CloseParen', 'Asterisk'):
	rule(terminal, StaticTerminal(terminal))
for terminal in ('Name', 'Integer', 'String', 'Float', 'Epsilon'):
	rule(terminal, globals()[terminal]())

postlude()