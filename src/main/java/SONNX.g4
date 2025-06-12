grammar SONNX;

options{
    caseInsensitive = true;
}

model
    : MODELPROTO '{' model_body_def '}'
    ;

model_body_def
    : ir_version_def producer_name_def producer_version_def domain_def model_version_def doc_string_def graph_def opset_import_def
    ;

ir_version_def
    : IR_VERSION '=' IntegerLiteral
    ;

producer_name_def
    : PRODUCER_NAME '=' StringLiteral
    ;

producer_version_def
    : PRODUCER_VERSION '=' StringLiteral
    ;

domain_def
    : DOMAIN '=' StringLiteral
    ;

model_version_def
    : MODEL_VERSION '=' IntegerLiteral
    ;

doc_string_def
    : DOC_STRING '=' StringLiteral
    ;

graph_def
    : GRAPH '{' graph_body_def '}'
    ;

graph_body_def
    : name_def node_list input_list output_list initializer_list?
    ;

name_def
    : NAME '=' StringLiteral
    ;
node_list
    : NODE '{' node_def '}' node_repeats
    ;

node_repeats
    : node_list node_repeats
    |
    ;

input_list
    : INPUT '{' value_info_def '}' input_repeats
    ;

input_repeats
    : input_list input_repeats
    |
    ;

output_list
    : OUTPUT '{' value_info_def '}' output_repeats
    ;

output_repeats
    : output_list output_repeats
    |
    ;

initializer_list
    : INITIALIZER '{' tensor_def '}' initializer_repeats
    ;

initializer_repeats
    : initializer_list initializer_repeats
    |
    ;

node_def
    : op_type_def name_def (input_list | input_arr) (output_list | output_arr) attribute_list?
    ;

op_type_def
    : OP_TYPE '=' StringLiteral
    ;

input_arr
    : INPUT '=' '[' StringLiteral id_repeats ']'
    ;

output_arr
    : OUTPUT '='  '[' StringLiteral id_repeats ']'
    ;

id_repeats
    : ',' StringLiteral id_repeats
    |
    ;

attribute_list
    : ATTRIBUTE '{' attribute_def '}' attribute_repeats
    ;

attribute_repeats
    : attribute_list attribute_repeats
    |
    ;

attribute_def
    : name_def value_def
    ;

value_def
    : VALUE '=' StringLiteral
    ;

value_info_def
    : name_def type_def
    ;

type_def
    : TYPE '{' tensor_type_def '}'
    ;

tensor_type_def
    : TENSOR_TYPE '{' elem_type_def shape_def '}'
    ;

elem_type_def
    : ELEM_TYPE '=' (INT | FLOAT | STRING | BOOL)
    ;

shape_def
    : SHAPE '{' dim_list '}'
    ;

dim_list
    : DIM '{' dim_def '}' dim_repeats
    ;

dim_repeats
    : dim_list dim_repeats
    |
    ;

dim_def
    : DIM_VALUE '=' IntegerLiteral | 'dim_param' '=' StringLiteral
    ;

tensor_def
    : name_def data_type_def dims_def raw_data_def
    ;

data_type_def
    : DATA_TYPE '=' (INT | FLOAT | STRING | BOOL)
    ;

dims_def
    : DIMS '=' IntegerLiteral dims_repeats
    ;


dims_repeats
    : IntegerLiteral dims_repeats
    |
    ;

raw_data_def
    : RAW_DATA '=' BytesLiteral
    ;

opset_import_def
    : OPSET_IMPORT '{' domain_def version_def '}'
    ;

version_def
    : VERSION '=' IntegerLiteral
    ;








// keywords

MODELPROTO : 'ModelProto';
GRAPH : 'graph';
NAME : 'name';
NODE : 'node';
INPUT : 'input';
OUTPUT : 'output';
OP_TYPE : 'op_type';
ATTRIBUTE : 'attribute';
INITIALIZER : 'initializer';
DOC_STRING : 'doc_string';
DOMAIN : 'domain';
MODEL_VERSION : 'model_version';
PRODUCER_NAME : 'producer_name';
PRODUCER_VERSION : 'producer_version';
TYPE : 'type';
TENSOR_TYPE : 'tensor_type';
IR_VERSION : 'ir_version';
ELEM_TYPE : 'elem_type';
SHAPE : 'shape';
DIM : 'dim';
DIMS : 'dims';
RAW_DATA : 'raw_data';
OPSET_IMPORT : 'opset_import';
DIM_VALUE : 'dim_value';
DATA_TYPE : 'data_type';
VERSION : 'version';
VALUE : 'value';

INT : 'int';
FLOAT : 'float';
STRING : 'string';
BOOL : 'bool';

// integer规则

IntegerLiteral: IntegerNumeral IntegerTypeSuffix?;
fragment IntegerTypeSuffix: [IL];
fragment IntegerNumeral: '0' | [1-9][0-9]* ;


// string规则
StringLiteral: '"'(EscapeSequence | ~[\\"])*'"';
fragment EscapeSequence: '\\' ('b' | 't' | 'n' | 'f' | 'r' | '"' | '\'' | '\\');

// bytes规则
BytesLiteral: [0-9a-f]+ BytesSuffix;
fragment BytesSuffix: 'b';

// 分隔符
LBRACE: '{';
RBRACE: '}';
LBRACK: '[';
RBRACK: ']';
COMMA: ',';

// 操作符
ASSIGN: '=';

// 杂项
DIM_PARAM: 'dim_param';

// 空白符
WS: [ \t\r\n\u000C\u3000]+ -> channel(HIDDEN);
COMMENT: '/*' .*? '*/' -> channel(HIDDEN);
LINE_COMMENT: '//' ~[\r\n]* -> channel(HIDDEN);