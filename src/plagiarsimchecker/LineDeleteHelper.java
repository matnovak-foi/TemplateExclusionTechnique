package plagiarsimchecker;

import org.foi.common.JavaCodePartsRemover;

public class LineDeleteHelper {
    JavaCodePartsRemover remover = new JavaCodePartsRemover();

    public String deleteOkLines(String block) {
        //System.out.println("------------ENDSTART1-----------------\n"+block+"\n------------END1-----------------");
        block = remover.removeStringValues(block);
        block = block.replaceAll("///\\*","//\n/*");
        block = block.replaceAll("//[^\\n]*\\*/","*/");
        block = remover.removeComments(block);
        //System.out.println("------------START-----------------");
        //System.out.println(block);
        //System.out.println("------------ENDSTARTX-----------------");
        block = remover.removeImportStatements(block);
        block = remover.removePackageStatements(block);
        block = remover.removeAnnotations(block);

        //System.out.println("------------ENDSTART1-----------------\n"+block+"\n------------END1-----------------");
        block = removeFullBlocks(block);
        //System.out.println("------------ENDSTART1-----------------\n"+block+"\n------------END1-----------------");
        block = remover.removeTryWithResources(block);
        //System.out.println("------------ENDSTART1-----------------\n"+block+"\n------------END1-----------------");
        //System.out.println("------------ENDSTARTX-----------------");
        //System.out.println(block);
        //System.out.println("------------ENDX-----------------");
        block = remover.removeSyncronizedThis(block);
        block = block.replaceAll("(^|)synchronized(\\s|$)"," ");
        block = block.replaceAll("(^|\\s)final(\\s|$)"," ");
        block = block.replaceAll("(^|\\s)abstract(\\s|$)"," ");
        block = block.replaceAll("(^|\\s)static\\s*\\{\\s*\\}"," ");
        block = block.replaceAll("(^|\\s)static(\\s|$)"," ");
        block = block.replaceAll("<T> "," ");
        //System.out.println("------------ENDSTARTX-----------------");
        //System.out.println(block);
        //System.out.println("------------ENDX-----------------");

        //System.out.println("------------ENDSTART1-----------------\n"+block+"\n------------END1-----------------");
        block = remover.removeSimpleStatements(block);
        //System.out.println("------------ENDSTART1-----------------");
        //System.out.println(block);
        //System.out.println("------------END1-----------------");
        block = remover.removeEmptyLoopBlockAndSimilar(block);
        block = remover.removeAnyEmptyMethod(block);
        block = remover.removeSimpleStatements(block);
        block = remover.removeEmptyClasses(block);
        block = remover.removeLeftoverWhiteSpaces(block);
        //System.out.println("------------ENDSTART-----------------");
        //System.out.println(block);
        //System.out.println("------------END-----------------");

        return block.trim();
    }

    private String removeFullBlocks(String block) {
        int blockSize = block.length();
        int closedBracketPosition = block.lastIndexOf("}");
        int openBracketPosition = block.lastIndexOf("{");

        if (openBracketPosition == -1 || closedBracketPosition == -1)
            return block;

        if (openBracketPosition > closedBracketPosition) {
            String substring = block.substring(0, openBracketPosition);
            block = removeFullBlocks(substring) + block.substring(openBracketPosition, blockSize);
        } else {
            openBracketPosition = findMatchigOpenBracketPosition(block, closedBracketPosition);
            if(openBracketPosition!=-1) {
                String substring = block.substring(0, openBracketPosition);
                block = removeFullBlocks(substring) + "{" + block.substring(closedBracketPosition, blockSize);
            }else {
                String substring = block.substring(0, closedBracketPosition);
                block = removeFullBlocks(substring) + block.substring(closedBracketPosition, blockSize);
            }
        }

        return block;
    }

    public int findMatchigOpenBracketPosition(String block, int endBracket) {
        int openBracket = block.lastIndexOf("{");

        if (openBracket == -1)
            return -1;

        String substring = block.substring(openBracket, endBracket);
        if (!substring.contains("}")) {
            return openBracket;
        }

        char[] letters = block.toCharArray();
        char open = "{".toCharArray()[0];
        char closed = "}".toCharArray()[0];
        int numOfClosed = 0;
        for (int i = letters.length - 1; i > 0; i--) {
            if (letters[i] == closed)
                numOfClosed++;

            if (letters[i] == open && numOfClosed > 0)
                numOfClosed--;

            if (letters[i] == open && numOfClosed == 0)
                return i;
        }

        return -1;
    }
}
