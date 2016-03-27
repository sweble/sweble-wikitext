/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sweble.wikitext.engine.ext.parser_functions;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class ExprParser
{
	private static final int maxStackSize = 100;

	private static final Map<String, Token> TOKENS = new HashMap<String, Token>();

	static
	{
		TOKENS.put("(", Token.LPAREN);
		TOKENS.put(")", Token.RPAREN);
		TOKENS.put("!=", Token.NEQ);
		TOKENS.put("*", Token.TIMES);
		TOKENS.put("+", Token.PLUS);
		//tokens.put("+", Token.POS);
		TOKENS.put("-", Token.MINUS);
		//tokens.put("-", Token.NEG);
		TOKENS.put("/", Token.DIVIDE);
		TOKENS.put("<", Token.LE);
		TOKENS.put("<=", Token.LEQ);
		TOKENS.put("<>", Token.NEQ);
		TOKENS.put("=", Token.EQ);
		TOKENS.put(">", Token.GR);
		TOKENS.put(">=", Token.GEQ);
		TOKENS.put("^", Token.POW);
		TOKENS.put("abs", Token.ABS);
		TOKENS.put("acos", Token.ARCCOS);
		TOKENS.put("and", Token.AND);
		TOKENS.put("asin", Token.ARCSINE);
		TOKENS.put("atan", Token.ARCTAN);
		TOKENS.put("ceil", Token.CEIL);
		TOKENS.put("cos", Token.COSINE);
		TOKENS.put("div", Token.DIVIDE);
		TOKENS.put("e", Token.E);
		//tokens.put("e", Token.SCIENTIFIC);
		TOKENS.put("exp", Token.EXP);
		TOKENS.put("floor", Token.FLOOR);
		TOKENS.put("ln", Token.LN);
		TOKENS.put("mod", Token.MOD);
		TOKENS.put("not", Token.NOT);
		TOKENS.put("or", Token.OR);
		TOKENS.put("pi", Token.PI);
		TOKENS.put("round", Token.ROUND);
		TOKENS.put("sin", Token.SINE);
		TOKENS.put("tan", Token.TANGENS);
		TOKENS.put("trunc", Token.TRUNC);
	}

	// =====================================================================

	private final Stack<Double> operands = new Stack<Double>();

	private final Stack<Token> operators = new Stack<Token>();

	private Production expecting;

	// =====================================================================

	/**
	 * http://montcs.bloomu.edu/~bobmon/Information/RPN/infix2rpn.shtml
	 * 
	 * @throws ExprError
	 */
	public String parse(String expr) throws ExprError
	{
		operands.clear();
		operators.clear();
		expecting = Production.EXPR;
		int i = 0;

		expr = unescape(expr);

		while (i < expr.length())
		{
			if (operands.size() > maxStackSize
					|| operators.size() > maxStackSize)
				throw new ExprError("operands_exhausted");

			char ch = expr.charAt(i);

			if (isWs(ch))
			{
				i = skipWs(expr, i);
				continue;
			}
			else if (isNumberChar(ch))
			{
				expect(Production.EXPR, "unexpected_number");
				i = pushOperand(expr, i);
				expecting = Production.OPERATOR;
				continue;
			}
			else
			{
				String word = null;
				Token token = null;
				if (isAlphaChar(ch))
				{
					word = parseWordToken(expr, i).toLowerCase();
					token = TOKENS.get(word);
				}
				else
				{
					if (i + 1 < expr.length())
					{
						// Try two-character operators
						word = expr.substring(i, i + 2);
						token = TOKENS.get(word);
					}

					if (token == null)
					{
						// Try one-character operators
						word = "" + ch;
						token = TOKENS.get(word);
					}
				}

				if (token == null)
					throw new ExprError("Unrecognised word \"%s\".", word);

				i += word.length();

				switch (token)
				{

				// -- Constants ----------------------------------------

					case E:
					{
						if (expecting == Production.OPERATOR)
						{
							processBinaryOp(Token.SCIENTIFIC, word);
							continue;
						}
						// Fall through to handle constants
					}
					case PI:
					{
						if (expecting == Production.EXPR)
						{
							token.apply(operands);
							expecting = Production.OPERATOR;
						}
						continue;
					}

					// -- Unary operators ----------------------------------

					case NOT:
					case SINE:
					case COSINE:
					case TANGENS:
					case ARCSINE:
					case ARCCOS:
					case ARCTAN:
					case EXP:
					case LN:
					case ABS:
					case FLOOR:
					case TRUNC:
					case CEIL:
					{
						expect(Production.EXPR, "unexpected_operator", word);
						operators.push(token);
						continue;
					}

					// -- Binary or Unary ----------------------------------

					case PLUS:
					case MINUS:
					{
						if (expecting == Production.EXPR)
						{
							operators.push(token == Token.PLUS ?
									Token.POS :
									Token.NEG);
						}
						else
						{
							processBinaryOp(token, word);
						}
						continue;
					}

					// -- Binary operators ---------------------------------

					case EQ:
					case NEQ:
					case LE:
					case GR:
					case LEQ:
					case GEQ:
					case TIMES:
					case DIVIDE:
					case MOD:
					case POW:
					case ROUND:
					case AND:
					case OR:
					{
						processBinaryOp(token, word);
						continue;
					}

					// -- Parentheses --------------------------------------

					case LPAREN:
					{
						expect(Production.EXPR, "unexpected_operator", word);
						operators.push(token);
						continue;
					}

					case RPAREN:
					{
						Token lastOp = null;
						while (!operators.isEmpty())
						{
							lastOp = operators.peek();
							if (lastOp == Token.LPAREN)
								break;

							lastOp.apply(operands);
							operators.pop();
						}

						if (lastOp != Token.LPAREN)
							throw new ExprError("unexpected_closing_bracket");

						operators.pop();
						expecting = Production.OPERATOR;
						continue;
					}

					default:
						throw new InternalError();
				}
			}
		}

		while (!operators.isEmpty())
		{
			Token op = operators.pop();
			if (op == Token.LPAREN)
				throw new ExprError("unclosed_bracket");

			op.apply(operands);
		}

		return implode("<br />\n", operands);
	}

	// =====================================================================

	private String unescape(String expr)
	{
		expr = expr.replace("&lt;", "<");
		expr = expr.replace("&gt;", ">");
		expr = expr.replace("&minus;", "-");
		expr = expr.replace("\u2212", "-");
		return expr;
	}

	// =====================================================================

	private boolean isWs(char ch)
	{
		return Character.isWhitespace(ch);
	}

	private int skipWs(String expr, int i)
	{
		int j = i + 1;
		while (j < expr.length() && isWs(expr.charAt(j)))
			++j;
		return j;
	}

	// =====================================================================

	private boolean isNumberChar(char ch)
	{
		return ch == '.' || Character.isDigit(ch);
	}

	private int pushOperand(String expr, int i)
	{
		int j = i + 1;
		while (j < expr.length())
		{
			char ch = expr.charAt(j);
			if (!isNumberChar(ch))
				break;
			++j;
		}

		try
		{
			operands.push(Double.parseDouble(expr.substring(i, j)));
		}
		catch (NumberFormatException e)
		{
			operands.push(0.);
		}

		return j;
	}

	// =====================================================================

	private boolean isAlphaChar(char ch)
	{
		return Character.isLetter(ch);
	}

	private String parseWordToken(String expr, int i)
	{
		int j = i + 1;
		while (j < expr.length())
		{
			char chx = expr.charAt(j);
			if (!isAlphaChar(chx))
				break;
			++j;
		}

		return expr.substring(i, j);
	}

	// =====================================================================

	private void expect(Production p, String msg) throws ExprError
	{
		if (expecting != p)
			throw new ExprError(msg);
	}

	private void expect(Production p, String msg, String word) throws ExprError
	{
		if (expecting != p)
			throw new ExprError(msg, word);
	}

	// =====================================================================

	private void processBinaryOp(Token op, String word) throws ExprError
	{
		expect(Production.OPERATOR, "unexpected_operator", word);

		while (!operators.isEmpty())
		{
			Token lastOp = operators.peek();
			if (op.getPrecedence() > lastOp.getPrecedence())
				break;

			lastOp.apply(operands);
			operators.pop();
		}

		operators.push(op);
		expecting = Production.EXPR;
	}

	// =====================================================================

	private String implode(String serparator, Stack<Double> operands)
	{
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < operands.size();)
		{
			double result = operands.get(i);
			if (((double) (int) result) == result)
				b.append((int) result);
			else
				b.append(result);
			if (++i < operands.size())
				b.append(serparator);
		}
		return b.toString();
	}

	// =========================================================================

	public static final class ExprError
			extends
				Exception
	{
		private static final long serialVersionUID = 1L;

		private final String param;

		public ExprError(String message)
		{
			this(message, null);
		}

		public ExprError(String message, String param)
		{
			super(makeMessage(message, param));
			this.param = param;
		}

		private static String makeMessage(String message, String param)
		{
			String msg = message;
			if (param != null)
				msg = String.format(message, param);
			return "Expression error: " + msg;
		}

		public String getParam()
		{
			return param;
		}
	}

	// =========================================================================

	private static enum Production
	{
		EXPR,
		OPERATOR;
	}

	// =========================================================================

	private static enum Token
	{
		// -- Constants -- e, pi --

		E(-1, "e")
		{
			@Override
			public void apply(Stack<Double> operands) throws ExprError
			{
				operands.push(Math.E);
			}
		},
		PI(-1, "pi")
		{
			@Override
			public void apply(Stack<Double> operands) throws ExprError
			{
				operands.push(Math.PI);
			}
		},

		// -- Binary -- 10e^x --

		SCIENTIFIC(10, "e")
		{
			@Override
			public void apply(Stack<Double> operands) throws ExprError
			{
				requireTwoOps(this, operands);
				double right = operands.pop();
				double left = operands.pop();
				operands.push(left * Math.pow(10, right));
			}
		},

		// -- Unary -- +, -, ! --

		POS(10, "+")
		{
			@Override
			public void apply(Stack<Double> operands) throws ExprError
			{
				requireOneOp(this, operands);
			}
		},
		NEG(10, "-")
		{
			@Override
			public void apply(Stack<Double> operands) throws ExprError
			{
				requireOneOp(this, operands);
				double arg = operands.pop();
				operands.push(-arg);
			}
		},

		NOT(9, "not")
		{
			@Override
			public void apply(Stack<Double> operands) throws ExprError
			{
				requireOneOp(this, operands);
				double arg = operands.pop();
				operands.push((arg == 0) ? 1. : 0.);
			}
		},

		// -- Unary -- sin, cos, tan, atan, acos, atan --

		SINE(9, "sin")
		{
			@Override
			public void apply(Stack<Double> operands) throws ExprError
			{
				requireOneOp(this, operands);
				double arg = operands.pop();
				operands.push(Math.sin(arg));
			}
		},
		COSINE(9, "cos")
		{
			@Override
			public void apply(Stack<Double> operands) throws ExprError
			{
				requireOneOp(this, operands);
				double arg = operands.pop();
				operands.push(Math.cos(arg));
			}
		},
		TANGENS(9, "tan")
		{
			@Override
			public void apply(Stack<Double> operands) throws ExprError
			{
				requireOneOp(this, operands);
				double arg = operands.pop();
				operands.push(Math.tan(arg));
			}
		},
		ARCSINE(9, "asin")
		{
			@Override
			public void apply(Stack<Double> operands) throws ExprError
			{
				requireOneOp(this, operands);
				double arg = operands.pop();
				if (arg < -1 || arg > 1)
					throw new ExprError("invalid_argument", toString());
				operands.push(Math.asin(arg));
			}
		},
		ARCCOS(9, "acos")
		{
			@Override
			public void apply(Stack<Double> operands) throws ExprError
			{
				requireOneOp(this, operands);
				double arg = operands.pop();
				if (arg < -1 || arg > 1)
					throw new ExprError("invalid_argument", toString());
				operands.push(Math.acos(arg));
			}
		},
		ARCTAN(9, "atan")
		{
			@Override
			public void apply(Stack<Double> operands) throws ExprError
			{
				requireOneOp(this, operands);
				double arg = operands.pop();
				operands.push(Math.atan(arg));
			}
		},

		// -- Unary -- e^x, ln(x) --

		EXP(9, "exp")
		{
			@Override
			public void apply(Stack<Double> operands) throws ExprError
			{
				requireOneOp(this, operands);
				double arg = operands.pop();
				operands.push(Math.exp(arg));
			}
		},
		LN(9, "ln")
		{
			@Override
			public void apply(Stack<Double> operands) throws ExprError
			{
				requireOneOp(this, operands);
				double arg = operands.pop();
				if (arg <= 0)
					throw new ExprError("invalid_argument_ln", toString());
				operands.push(Math.log(arg));
			}
		},

		// -- Unary -- abs, floor, trunc, ceil --

		ABS(9, "abs")
		{
			@Override
			public void apply(Stack<Double> operands) throws ExprError
			{
				requireOneOp(this, operands);
				double arg = operands.pop();
				operands.push(Math.abs(arg));
			}
		},

		FLOOR(9, "floor")
		{
			@Override
			public void apply(Stack<Double> operands) throws ExprError
			{
				requireOneOp(this, operands);
				double arg = operands.pop();
				operands.push(Math.floor(arg));
			}
		},
		TRUNC(9, "trunc")
		{
			@Override
			public void apply(Stack<Double> operands) throws ExprError
			{
				requireOneOp(this, operands);
				double arg = operands.pop();
				operands.push((double) ((int) arg));
			}
		},
		CEIL(9, "ceil")
		{
			@Override
			public void apply(Stack<Double> operands) throws ExprError
			{
				requireOneOp(this, operands);
				double arg = operands.pop();
				operands.push(Math.ceil(arg));
			}
		},

		// -- Binary -- --

		POW(8, "^")
		{
			@Override
			public void apply(Stack<Double> operands) throws ExprError
			{
				requireTwoOps(this, operands);
				double right = operands.pop();
				double left = operands.pop();
				double result = Math.pow(left, right);
				if (Double.isNaN(result))
					throw new ExprError("division_by_zero", toString());
				operands.push(result);
			}
		},
		TIMES(7, "*")
		{
			@Override
			public void apply(Stack<Double> operands) throws ExprError
			{
				requireTwoOps(this, operands);
				double right = operands.pop();
				double left = operands.pop();
				operands.push(left * right);
			}
		},
		DIVIDE(7, "/")
		{
			@Override
			public void apply(Stack<Double> operands) throws ExprError
			{
				requireTwoOps(this, operands);
				double right = operands.pop();
				double left = operands.pop();
				if (right == 0)
					throw new ExprError("division_by_zero", toString());
				operands.push(left / right);
			}
		},
		MOD(7, "mod")
		{
			@Override
			public void apply(Stack<Double> operands) throws ExprError
			{
				requireTwoOps(this, operands);
				double right = operands.pop();
				double left = operands.pop();
				if (right == 0)
					throw new ExprError("division_by_zero", toString());
				operands.push(left % right);
			}
		},

		// -- Binary -- --

		PLUS(6, "+")
		{
			@Override
			public void apply(Stack<Double> operands) throws ExprError
			{
				requireTwoOps(this, operands);
				double right = operands.pop();
				double left = operands.pop();
				operands.push(left + right);
			}
		},
		MINUS(6, "-")
		{
			@Override
			public void apply(Stack<Double> operands) throws ExprError
			{
				requireTwoOps(this, operands);
				double right = operands.pop();
				double left = operands.pop();
				operands.push(left - right);
			}
		},

		// -- Binary -- round --

		ROUND(5, "round")
		{
			@Override
			public void apply(Stack<Double> operands) throws ExprError
			{
				requireTwoOps(this, operands);
				int digits = (int) (double) operands.pop();
				double value = operands.pop();
				value = round(value, digits);
				operands.push(value);
			}
		},

		// -- Binary -- --

		EQ(4, "=")
		{
			@Override
			public void apply(Stack<Double> operands) throws ExprError
			{
				requireTwoOps(this, operands);
				double right = operands.pop();
				double left = operands.pop();
				operands.push((left == right) ? 1. : 0.);
			}
		},
		NEQ(4, "!=")
		{
			@Override
			public void apply(Stack<Double> operands) throws ExprError
			{
				requireTwoOps(this, operands);
				double right = operands.pop();
				double left = operands.pop();
				operands.push((left != right) ? 1. : 0.);
			}
		},
		LE(4, "<")
		{
			@Override
			public void apply(Stack<Double> operands) throws ExprError
			{
				requireTwoOps(this, operands);
				double right = operands.pop();
				double left = operands.pop();
				operands.push((left < right) ? 1. : 0.);
			}
		},
		GR(4, ">")
		{
			@Override
			public void apply(Stack<Double> operands) throws ExprError
			{
				requireTwoOps(this, operands);
				double right = operands.pop();
				double left = operands.pop();
				operands.push((left > right) ? 1. : 0.);
			}
		},
		LEQ(4, "<=")
		{
			@Override
			public void apply(Stack<Double> operands) throws ExprError
			{
				requireTwoOps(this, operands);
				double right = operands.pop();
				double left = operands.pop();
				operands.push((left <= right) ? 1. : 0.);
			}
		},
		GEQ(4, ">=")
		{
			@Override
			public void apply(Stack<Double> operands) throws ExprError
			{
				requireTwoOps(this, operands);
				double right = operands.pop();
				double left = operands.pop();
				operands.push((left >= right) ? 1. : 0.);
			}
		},

		// -- Binary -- --

		AND(3, "and")
		{
			@Override
			public void apply(Stack<Double> operands) throws ExprError
			{
				requireTwoOps(this, operands);
				double right = operands.pop();
				double left = operands.pop();
				operands.push((left != 0 && right != 0) ? 1. : 0.);
			}
		},
		OR(2, "or")
		{
			@Override
			public void apply(Stack<Double> operands) throws ExprError
			{
				requireTwoOps(this, operands);
				double right = operands.pop();
				double left = operands.pop();
				operands.push((left != 0 || right != 0) ? 1. : 0.);
			}
		},

		// -- Binary -- --

		LPAREN(-1, "(")
		{
			@Override
			public void apply(Stack<Double> operands)
			{
				throw new InternalError();
			}
		},
		RPAREN(-1, ")")
		{
			@Override
			public void apply(Stack<Double> operands)
			{
				throw new InternalError();
			}
		};

		// -----------------------------------------------------------------

		private final int precedence;

		private final String name;

		// -----------------------------------------------------------------

		Token(int precedence, String name)
		{
			this.name = name;
			this.precedence = precedence;
		}

		// -----------------------------------------------------------------

		public abstract void apply(Stack<Double> operands) throws ExprError;

		public int getPrecedence()
		{
			return precedence;
		}

		@Override
		public String toString()
		{
			return name;
		}

		// -----------------------------------------------------------------

		private static void requireOneOp(Token op, Stack<Double> operands) throws ExprError
		{
			if (operands.isEmpty())
				throw new ExprError("Missing operand for %s.", op.toString());
		}

		private static void requireTwoOps(Token op, Stack<Double> operands) throws ExprError
		{
			if (operands.size() < 2)
				throw new ExprError("Missing operand for %s.", op.toString());
		}

		private static double round(double value, int digits)
		{
			return new BigDecimal(value)
					.setScale(digits, BigDecimal.ROUND_HALF_UP)
					.doubleValue();
		}
	}
}
