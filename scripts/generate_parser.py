#!/usr/bin/env python
from grammar_helpers import *

prelude()

rule('Statement', OneOf(
	'InsertStatement', 'SelectStatement', 'DeleteStatement', 'UpdateStatement',
	'DropTableStatement', 'CreateTableStatement',
	'SaveStatement', 'LoadStatement', 'CreateDatabaseStatement', 'DropDatabaseStatement',
	'EvalStatement',
	'QuitStatement'))

rule('InsertStatement', Sequence(
	'InsertKeyword', Definite(),
	'IntoKeyword',
	('Name', 'tableName'),
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
	('Name', 'tableName'),
	('MaybeWhereClause', 'whereClause'),
	'Semicolon'))
rule('MaybeColumnSet', OneOf('ColumnList', 'Asterisk'))
rule('MaybeWhereClause', OneOf('WhereClause', 'Epsilon'))

rule('DeleteStatement', Sequence(
	'DeleteKeyword', Definite(),
	'FromKeyword',
	('Name', 'tableName'),
	('MaybeWhereClause', 'whereClause'),
	'Semicolon'))

rule('UpdateStatement', Sequence(
	'UpdateKeyword', Definite(),
	('Name', 'tableName'),
	'SetKeyword',
	('UpdateFieldList', 'updateFields'),
	('MaybeWhereClause', 'whereClause'),
	'Semicolon'))
rule('UpdateFieldList', OneOf(
	Sequence(('UpdateField', 'this'), 'Comma', ('UpdateField', 'next')),
	'UpdateField'))
rule('UpdateField', Sequence(
	('Name', 'columnName'),
	'SingleEqOp',
	('Expression', 'value')))

rule('SaveStatement', Sequence(
	'SaveOrCommitKeyword', Definite(),
	'Semicolon'))
rule('SaveOrCommitKeyword', OneOf('SaveKeyword', 'CommitKeyword'))

rule('LoadStatement', Sequence(
	'LoadKeyword', Definite(),
	('Name', 'dbName'),
	'Semicolon'))

rule('CreateDatabaseStatement', Sequence(
	'CreateKeyword', 'DatabaseKeyword', Definite(),
	('Name', 'dbName'),
	'Semicolon'))

rule('DropDatabaseStatement', Sequence(
	'DropKeyword', 'DatabaseKeyword', Definite(),
	('Name', 'dbName'),
	'Semicolon'))

rule('DropTableStatement', Sequence(
	'DropKeyword', 'TableKeyword', Definite(),
	('Name', 'tableName'),
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
	('Name', 'tableName'),
	'OpenParen',
	('FieldDefList', 'columns'),
	'CloseParen',
	'Semicolon'))
rule('FieldDefList', OneOf(
	Sequence(('FieldDef', 'this'), 'Comma', ('FieldDefList', 'next')),
	'FieldDef'))
rule('FieldDef', Sequence(
	('Name', 'columnName'), Definite(),
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

rule('Expression', OneOf('LogicalExpression', 'SingleValue'))
rule('LogicalExpression', OneOf(
	Sequence(('NotExpression', 'lhs'), ('LogicalOp', 'operator'), ('LogicalExpression', 'rhs')),
	'NotExpression'))
rule('NotExpression', OneOf(
	Sequence('NotKeyword', ('Comparison', 'operand')),
	'Comparison'))
rule('LogicalOp', OneOf('AndKeyword', 'OrKeyword'))
rule('Comparison', OneOf('LtComparison', 'GtComparison', 'EqComparison', 'NeComparison', 'LeComparison', 'GeComparison', 'SingleValue'))
rule('LtComparison', Sequence(('SingleValue', 'lhs'), 'LtOp', ('Comparison', 'rhs')))
rule('GtComparison', Sequence(('SingleValue', 'lhs'), 'GtOp', ('Comparison', 'rhs')))
rule('EqComparison', Sequence(('SingleValue', 'lhs'), 'EqOp', ('Comparison', 'rhs')))
rule('NeComparison', Sequence(('SingleValue', 'lhs'), 'NeOp', ('Comparison', 'rhs')))
rule('LeComparison', Sequence(('SingleValue', 'lhs'), 'LeOp', ('Comparison', 'rhs')))
rule('GeComparison', Sequence(('SingleValue', 'lhs'), 'GeOp', ('Comparison', 'rhs')))
rule('SingleValue', OneOf('String', 'Integer', 'Float', 'Name'))

for kw in ("INSERT", "SELECT", "UPDATE", "DROP", "DELETE", "CREATE", "INTO", "FROM", "VALUES", "TABLE",
	"SAVE", "COMMIT", "LOAD", "DATABASE", "QUIT", "NOT", "NULL", "WHERE", "EVAL", "SET", "AND", "OR"):
	rule(kw.title() + 'Keyword', Keyword(kw))
for op in ('LT', 'GT', 'EQ', 'NE', 'LE', 'GE', 'SINGLE_EQ'):
	rule(op.title().replace('_', '') + 'Op', Operator(op))
for terminal in ('Semicolon', 'Comma', 'OpenParen', 'CloseParen', 'Asterisk'):
	rule(terminal, StaticTerminal(terminal))
for terminal in ('Name', 'Integer', 'String', 'Float', 'Epsilon'):
	rule(terminal, globals()[terminal]())

postlude()