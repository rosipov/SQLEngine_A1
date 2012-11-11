#!/usr/bin/env python
from grammar_helpers import *

prelude()

rule('Statement', OneOf(
	'InsertStatement', 'SelectStatement',
	'DropTableStatement', 'CreateTableStatement',
	'SaveStatement', 'LoadStatement', 'CreateDatabaseStatement', 'DropDatabaseStatement',
	'EvalStatement',
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
	('Name', 'tablename'),
	('MaybeWhereClause', 'where-clause'),
	'Semicolon'))
rule('MaybeColumnSet', OneOf('ColumnList', 'Asterisk'))
rule('MaybeWhereClause', OneOf('WhereClause', 'Epsilon'))

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

rule('EvalStatement', Sequence(
	'EvalKeyword', Definite(),
	('Expression', 'expression'),
	'Semicolon'))

rule('QuitStatement', Sequence(
	'QuitKeyword', Definite(),
	'Semicolon'))

rule('CreateTableStatement', Sequence(
	'CreateKeyword', 'TableKeyword', Definite(),
	('Name', 'tablename'),
	'OpenParen',
	('FieldDefList', 'columns'),
	'CloseParen',
	'Semicolon'))
rule('FieldDefList', OneOf(
	Sequence(('FieldDef', 'this'), 'Comma', ('FieldDefList', 'next')),
	'FieldDef'))
rule('FieldDef', Sequence(
	('Name', 'columnname'), Definite(),
	('Name', 'type'),
	('ColumnLength', 'length'),
	('ColumnNullity', 'nullity')))
rule('ColumnLength', OneOf(
	Sequence('OpenParen', ('Integer', 'length'), 'CloseParen'),
	'Epsilon'))
rule('ColumnNullable', Sequence(
	'NullKeyword'))
rule('ColumnNotNullable', Sequence(
	'NotKeyword', 'NullKeyword'))
rule('ColumnNullity', OneOf(
	'ColumnNullable',
	'ColumnNotNullable',
	'Epsilon'))

rule('WhereClause', Sequence(
	'WhereKeyword', Definite(),
	('Expression', 'condition')))

rule('Expression', OneOf('Comparison'))
rule('Comparison', OneOf('LtComparison'))
rule('LtComparison', OneOf(Sequence(('SingleValue', 'lhs'), 'LtOp', ('Comparison', 'rhs')), 'SingleValue'))
rule('GtComparison', OneOf(Sequence(('SingleValue', 'lhs'), 'GtOp', ('Comparison', 'rhs')), 'SingleValue'))
rule('EqComparison', OneOf(Sequence(('SingleValue', 'lhs'), 'EqOp', ('Comparison', 'rhs')), 'SingleValue'))
rule('NeComparison', OneOf(Sequence(('SingleValue', 'lhs'), 'NeOp', ('Comparison', 'rhs')), 'SingleValue'))
rule('LeComparison', OneOf(Sequence(('SingleValue', 'lhs'), 'LeOp', ('Comparison', 'rhs')), 'SingleValue'))
rule('GeComparison', OneOf(Sequence(('SingleValue', 'lhs'), 'GeOp', ('Comparison', 'rhs')), 'SingleValue'))
rule('SingleValue', OneOf('String', 'Integer', 'Float', 'Name'))

for kw in ("INSERT", "SELECT", "UPDATE", "DROP", "DELETE", "CREATE", "INTO", "FROM", "VALUES", "TABLE", "SAVE", "COMMIT", "LOAD", "DATABASE", "QUIT", "NOT", "NULL", "WHERE", "EVAL"):
	rule(kw.title() + 'Keyword', Keyword(kw))
for op in ('LT', 'GT', 'EQ', 'NE', 'LE', 'GE'):
	rule(op.title() + 'Op', Operator(op))
for terminal in ('Semicolon', 'Comma', 'OpenParen', 'CloseParen', 'Asterisk'):
	rule(terminal, StaticTerminal(terminal))
for terminal in ('Name', 'Integer', 'String', 'Float', 'Epsilon'):
	rule(terminal, globals()[terminal]())

postlude()