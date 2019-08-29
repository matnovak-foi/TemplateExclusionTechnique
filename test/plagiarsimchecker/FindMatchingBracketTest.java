package plagiarsimchecker;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FindMatchingBracketTest {
    LineDeleteHelper deleteHelper = new LineDeleteHelper();

    @Test
    public void whenMatchihgBracketExistisWithInnerBrackets(){
        String block = "public boolean equals(Object object) {\n" +
                "        // TODO: Warning - this method won't work in the case the id fields are not set\n" +
                "        if (!(object instanceof Dnevnik)) {}\n" +
                "        Dnevnik other = (Dnevnik) object;\n" +
                "        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {}\n" +
                "        return true;\n" +
                "    }";
        int openBrackt = deleteHelper.findMatchigOpenBracketPosition(block,block.lastIndexOf("}"));
        assertEquals(block.indexOf("{"),openBrackt);
    }

    @Test
    public void whenMatchihgBracketExistisWithOutInnerBrackets(){
        String block = "public boolean equals(Object object) {\n" +
                "    }";
        int openBrackt = deleteHelper.findMatchigOpenBracketPosition(block,block.lastIndexOf("}"));
        assertEquals(block.indexOf("{"),openBrackt);
    }

    @Test
    public void whenMatchingBracketDoesNotExistAndWithoutInnerBrackets(){
        String block = "" +
                "        // TODO: Warning - this method won't work in the case the id fields are not set\n" +
                "        "+
                "    }";
        int openBrackt = deleteHelper.findMatchigOpenBracketPosition(block,block.lastIndexOf("}"));
        assertEquals(-1,openBrackt);
    }

    @Test
    public void whenMatchingBracketDoesNotExistButInnerBracketsExist(){
        String block = "" +
                "        // TODO: Warning - this method won't work in the case the id fields are not set\n" +
                "        if (!(object instanceof Dnevnik)) {}\n" +
                "        Dnevnik other = (Dnevnik) object;\n" +
                "        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {}\n" +
                "        return true;\n" +
                "    }";
        int openBrackt = deleteHelper.findMatchigOpenBracketPosition(block,block.lastIndexOf("}"));
        assertEquals(-1,openBrackt);
    }


}
