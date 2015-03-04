package absyn;

import types.Type;
import types.ClassType;
import types.CodeSignature;
import symbol.Symbol;
import semantical.TypeChecker;
import translate.Block;
import bytecode.NEWSTRING;

/**
 * A node of abstract syntax representing a string literal.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class StringLiteral extends Literal {

    /**
     * The lexical value of the string literal.
     */

    private String value;

    /**
     * Constructs the abstract syntax of a string literal.
     *
     * @param pos the position in the source file where it starts
     *            the concrete syntax represented by this abstract syntax
     * @param value the lexical value of the string literal
     */

    public StringLiteral(int pos, String value) {
	super(pos);

	this.value = value;
    }

    /**
     * Yields the lexical value of the string literal.
     *
     * @return the lexical value of the string literal
     */

    public String getValue() {
	return value;
    }

    /**
     * Yields a the string labelling the class
     * of abstract syntax represented by this string literal.
     * We redefine this method in order to include the lexical value
     * of the string literal.
     *
     * @return a string describing the kind of this node of abstract syntax,
     *         followed by the lexical value of this string literal
     */

    protected String label() {
	// in the string literal, we substitute the newline character with
	// an escape sequence so that it is literally reported by the dot
	// compiler
	return super.label() + ": " + value.replaceAll("\n","\\\\\\\\n");
    }

    /**
     * Performs the type-checking of a string literal.
     * There is nothing to check.
     *
     * @param checker the type-checker to be used for type-checking
     * @return the semantical <tt>String</tt> class type
     */

    protected Type typeCheck$0(TypeChecker checker) {
	// we type-check the <tt>String</tt> type since it is the only
	// class type which can be used in a program without
	// an explicit reference to its name (through constants like this)
	ClassType result = ClassType.mk(Symbol.STRING);

	// if <tt>String.kit</tt> cannot be found, we get an open class type here!
	if (result != null)
	    result.typeCheck();

	return result;
    }

    /**
     * Translates this expression into its intermediate Kitten code.
     * The result is a piece of code which pushes onto the stack
     * the value of the expression (namely, a <tt>newstring</tt> bytecode
     * which loads on the stack the lexical value of this string
     * literal expression) followed by the given <tt>continuation</tt>.
     * The original stack elements are not modified.
     *
     * @param where the method or constructor where this expression occurs
     * @param continuation the code executed after this expression
     * @return the code which evaluates this expression and continues
     *         with <tt>continuation</tt>
     */

    public Block translate(CodeSignature where, Block continuation) {
	return new NEWSTRING(where,value).followedBy(continuation);
    }
}
