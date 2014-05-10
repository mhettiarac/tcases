package org.cornutum.tcases.maven;

import org.cornutum.tcases.Tcases;

import org.apache.maven.plugin.testing.MojoRule;
import org.codehaus.plexus.PlexusTestCase;
import org.codehaus.plexus.util.DirectoryScanner;

import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;

/**
 * Runs tests for the {@link TcaseMojo} plugin.
 */
public class TcasesMojoTest
  {
  @Test
  public void withConfigDefault() throws Exception
    {
    // Given...
    File baseDirTest = getBaseDirTest( "tcases-project-default");

    // When...
    TcasesMojo tcasesMojo = (TcasesMojo) mojoHelper.lookupConfiguredMojo( baseDirTest, "tcases");
    tcasesMojo.execute();

    // Then...
    File expectedInputDir = new File( baseDirTest, "src/test/tcases");
    assertEquals( "Input dir", expectedInputDir, tcasesMojo.getInputDirFile());

    File expectedOutDir = new File( baseDirTest, "target/tcases");
    assertEquals( "Out dir", expectedOutDir, tcasesMojo.getOutDirFile());

    String[] expectedInputDefs = findPathsMatching( expectedInputDir, "**/*-Input.xml");
    assertEquals( "Input defs", 1, expectedInputDefs.length);

    String[] expectedTestDefs = findPathsMatching( expectedOutDir, "**/*");
    assertEquals( "Test defs", 1, expectedTestDefs.length);

    File expectedInputDef = new File( expectedInputDefs[0]);
    File expectedTestDef = new File( expectedInputDef.getParent(), Tcases.getProjectName( expectedInputDef) + "-Test.xml");
    assertEquals( "Test def", expectedTestDef.getPath(), expectedTestDefs[0]);
    }
  
  @Test
  public void withConfigCustom() throws Exception
    {
    // Given...
    File baseDirTest = getBaseDirTest( "tcases-project-custom");

    // When...
    TcasesMojo tcasesMojo = (TcasesMojo) mojoHelper.lookupConfiguredMojo( baseDirTest, "tcases");
    tcasesMojo.execute();

    // Then...
    File expectedInputDir = new File( baseDirTest, "custom");
    assertEquals( "Input dir", expectedInputDir, tcasesMojo.getInputDirFile());

    File expectedOutDir = new File( baseDirTest, "target/custom");
    assertEquals( "Out dir", expectedOutDir, tcasesMojo.getOutDirFile());

    String[] expectedInputDefs = findPathsMatching( expectedInputDir, "**/Input.xml", "**/InputModel");
    assertEquals( "Input defs", 2, expectedInputDefs.length);

    String[] expectedTestDefs = findPathsMatching( expectedOutDir, "**/*");
    assertEquals( "Test defs", 2, expectedTestDefs.length);
    }
  
  /**
   * Tests {@link TcasesMojo#execute execute()} using the following inputs.
   * <P>
   * <TABLE border="1" cellpadding="8">
   * <TR align="left"><TH colspan=2> 0. execute (Success) </TH></TR>
   * <TR align="left"><TH> Input Choice </TH> <TH> Value </TH></TR>
   * <TR><TD> InputDefPatterns.Count </TD> <TD> Many </TD> </TR>
   * <TR><TD> InputDefPatterns.Matched </TD> <TD> Many </TD> </TR>
   * <TR><TD> InputDir </TD> <TD> Other </TD> </TR>
   * <TR><TD> OutDir </TD> <TD> Other </TD> </TR>
   * <TR><TD> OutFile.Defined </TD> <TD> Yes </TD> </TR>
   * <TR><TD> OutFile.Wildcard </TD> <TD> None </TD> </TR>
   * <TR><TD> TestDef.Defined </TD> <TD> Yes </TD> </TR>
   * <TR><TD> TestDef.Wildcard </TD> <TD> One </TD> </TR>
   * <TR><TD> TestDef.Exists </TD> <TD> Yes </TD> </TR>
   * <TR><TD> GenDef.Defined </TD> <TD> Yes </TD> </TR>
   * <TR><TD> GenDef.Wildcard </TD> <TD> None </TD> </TR>
   * <TR><TD> GenDef.Exists </TD> <TD> Yes </TD> </TR>
   * <TR><TD> TransformDef.Defined </TD> <TD> Yes </TD> </TR>
   * <TR><TD> TransformDef.Relative </TD> <TD> Yes </TD> </TR>
   * <TR><TD> TransformDef.Wildcard </TD> <TD> None </TD> </TR>
   * <TR><TD> TransformDef.Exists </TD> <TD> Yes </TD> </TR>
   * <TR><TD> TransformParams.Defined </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Junit </TD> <TD> No </TD> </TR>
   * <TR><TD> NewTests </TD> <TD> No </TD> </TR>
   * <TR><TD> Seed.Defined </TD> <TD> No </TD> </TR>
   * <TR><TD> DefaultTupleSize.Defined </TD> <TD> Yes </TD> </TR>
   * </TABLE>
   * </P>
   */
  @Test
  public void whenInputPatternsMany() throws Exception
    {
    // Given...
    File baseDirTest = getBaseDirTest( "tcases-project-many");

    // When...
    TcasesMojo tcasesMojo = (TcasesMojo) mojoHelper.lookupConfiguredMojo( baseDirTest, "tcases");
    tcasesMojo.execute();

    // Then...
    File expectedInputDir = new File( baseDirTest, "tcases/input");
    assertEquals( "Input dir", expectedInputDir, tcasesMojo.getInputDirFile());

    File expectedOutDir = new File( baseDirTest, "target/tcases/output");
    assertEquals( "Out dir", expectedOutDir, tcasesMojo.getOutDirFile());

    String[] expectedInputDefs = findPathsMatching( expectedInputDir, "Input.xml", "*/The*Input.xml", "**/Other.xml");
    assertEquals( "Input defs", 3, expectedInputDefs.length);

    String[] expectedTestDefs = findPathsMatching( expectedOutDir, "**/*");
    assertEquals( "Test defs", 3, expectedTestDefs.length);
    }

  /**
   * Tests {@link TcasesMojo#execute execute()} using the following inputs.
   * <P>
   * <TABLE border="1" cellpadding="8">
   * <TR align="left"><TH colspan=2> 1. execute (Success) </TH></TR>
   * <TR align="left"><TH> Input Choice </TH> <TH> Value </TH></TR>
   * <TR><TD> InputDefPatterns.Count </TD> <TD> None </TD> </TR>
   * <TR><TD> InputDefPatterns.Matched </TD> <TD> One </TD> </TR>
   * <TR><TD> InputDir </TD> <TD> Default </TD> </TR>
   * <TR><TD> OutDir </TD> <TD> Default </TD> </TR>
   * <TR><TD> OutFile.Defined </TD> <TD> No </TD> </TR>
   * <TR><TD> OutFile.Wildcard </TD> <TD> NA </TD> </TR>
   * <TR><TD> TestDef.Defined </TD> <TD> Yes </TD> </TR>
   * <TR><TD> TestDef.Wildcard </TD> <TD> None </TD> </TR>
   * <TR><TD> TestDef.Exists </TD> <TD> Yes </TD> </TR>
   * <TR><TD> GenDef.Defined </TD> <TD> No </TD> </TR>
   * <TR><TD> GenDef.Wildcard </TD> <TD> NA </TD> </TR>
   * <TR><TD> GenDef.Exists </TD> <TD> NA </TD> </TR>
   * <TR><TD> TransformDef.Defined </TD> <TD> No </TD> </TR>
   * <TR><TD> TransformDef.Relative </TD> <TD> NA </TD> </TR>
   * <TR><TD> TransformDef.Wildcard </TD> <TD> NA </TD> </TR>
   * <TR><TD> TransformDef.Exists </TD> <TD> NA </TD> </TR>
   * <TR><TD> TransformParams.Defined </TD> <TD> No </TD> </TR>
   * <TR><TD> Junit </TD> <TD> Yes </TD> </TR>
   * <TR><TD> NewTests </TD> <TD> Default </TD> </TR>
   * <TR><TD> Seed.Defined </TD> <TD> Yes </TD> </TR>
   * <TR><TD> DefaultTupleSize.Defined </TD> <TD> No </TD> </TR>
   * </TABLE>
   * </P>
   */
  @Test
  public void whenInputPatternsNone() throws Exception
    {
    // Given...
    File baseDirTest = getBaseDirTest( "tcases-project-none");

    // When...
    TcasesMojo tcasesMojo = (TcasesMojo) mojoHelper.lookupConfiguredMojo( baseDirTest, "tcases");
    tcasesMojo.execute();

    // Then...
    File expectedInputDir = new File( baseDirTest, "tcases/input");
    assertEquals( "Input dir", expectedInputDir, tcasesMojo.getInputDirFile());

    File expectedOutDir = new File( baseDirTest, "target/tcases/output");
    assertEquals( "Out dir", expectedOutDir, tcasesMojo.getOutDirFile());

    String[] expectedInputDefs = findPathsMatching( expectedInputDir, "Input.xml", "*/The*Input.xml", "**/Other.xml");
    assertEquals( "Input defs", 3, expectedInputDefs.length);

    String[] expectedTestDefs = findPathsMatching( expectedOutDir, "**/*");
    assertEquals( "Test defs", 3, expectedTestDefs.length);
    }

  /**
   * Tests {@link TcasesMojo#execute execute()} using the following inputs.
   * <P>
   * <TABLE border="1" cellpadding="8">
   * <TR align="left"><TH colspan=2> 2. execute (Success) </TH></TR>
   * <TR align="left"><TH> Input Choice </TH> <TH> Value </TH></TR>
   * <TR><TD> InputDefPatterns.Count </TD> <TD> One </TD> </TR>
   * <TR><TD> InputDefPatterns.Matched </TD> <TD> Many </TD> </TR>
   * <TR><TD> InputDir </TD> <TD> Default </TD> </TR>
   * <TR><TD> OutDir </TD> <TD> Default </TD> </TR>
   * <TR><TD> OutFile.Defined </TD> <TD> Yes </TD> </TR>
   * <TR><TD> OutFile.Wildcard </TD> <TD> One </TD> </TR>
   * <TR><TD> TestDef.Defined </TD> <TD> No </TD> </TR>
   * <TR><TD> TestDef.Wildcard </TD> <TD> NA </TD> </TR>
   * <TR><TD> TestDef.Exists </TD> <TD> NA </TD> </TR>
   * <TR><TD> GenDef.Defined </TD> <TD> Yes </TD> </TR>
   * <TR><TD> GenDef.Wildcard </TD> <TD> One </TD> </TR>
   * <TR><TD> GenDef.Exists </TD> <TD> Yes </TD> </TR>
   * <TR><TD> TransformDef.Defined </TD> <TD> Yes </TD> </TR>
   * <TR><TD> TransformDef.Relative </TD> <TD> No </TD> </TR>
   * <TR><TD> TransformDef.Wildcard </TD> <TD> One </TD> </TR>
   * <TR><TD> TransformDef.Exists </TD> <TD> Yes </TD> </TR>
   * <TR><TD> TransformParams.Defined </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Junit </TD> <TD> Default </TD> </TR>
   * <TR><TD> NewTests </TD> <TD> No </TD> </TR>
   * <TR><TD> Seed.Defined </TD> <TD> No </TD> </TR>
   * <TR><TD> DefaultTupleSize.Defined </TD> <TD> No </TD> </TR>
   * </TABLE>
   * </P>
   */
  @Test
  public void execute_2()
    {
    // Given...

    // When...

    // Then...
    }

  /**
   * Tests {@link TcasesMojo#execute execute()} using the following inputs.
   * <P>
   * <TABLE border="1" cellpadding="8">
   * <TR align="left"><TH colspan=2> 3. execute (Success) </TH></TR>
   * <TR align="left"><TH> Input Choice </TH> <TH> Value </TH></TR>
   * <TR><TD> InputDefPatterns.Count </TD> <TD> Many </TD> </TR>
   * <TR><TD> InputDefPatterns.Matched </TD> <TD> None </TD> </TR>
   * <TR><TD> InputDir </TD> <TD> Default </TD> </TR>
   * <TR><TD> OutDir </TD> <TD> NA </TD> </TR>
   * <TR><TD> OutFile.Defined </TD> <TD> NA </TD> </TR>
   * <TR><TD> OutFile.Wildcard </TD> <TD> NA </TD> </TR>
   * <TR><TD> TestDef.Defined </TD> <TD> NA </TD> </TR>
   * <TR><TD> TestDef.Wildcard </TD> <TD> NA </TD> </TR>
   * <TR><TD> TestDef.Exists </TD> <TD> NA </TD> </TR>
   * <TR><TD> GenDef.Defined </TD> <TD> NA </TD> </TR>
   * <TR><TD> GenDef.Wildcard </TD> <TD> NA </TD> </TR>
   * <TR><TD> GenDef.Exists </TD> <TD> NA </TD> </TR>
   * <TR><TD> TransformDef.Defined </TD> <TD> NA </TD> </TR>
   * <TR><TD> TransformDef.Relative </TD> <TD> NA </TD> </TR>
   * <TR><TD> TransformDef.Wildcard </TD> <TD> NA </TD> </TR>
   * <TR><TD> TransformDef.Exists </TD> <TD> NA </TD> </TR>
   * <TR><TD> TransformParams.Defined </TD> <TD> NA </TD> </TR>
   * <TR><TD> Junit </TD> <TD> NA </TD> </TR>
   * <TR><TD> NewTests </TD> <TD> NA </TD> </TR>
   * <TR><TD> Seed.Defined </TD> <TD> NA </TD> </TR>
   * <TR><TD> DefaultTupleSize.Defined </TD> <TD> NA </TD> </TR>
   * </TABLE>
   * </P>
   */
  @Test
  public void execute_3()
    {
    // Given...

    // When...

    // Then...
    }

  /**
   * Tests {@link TcasesMojo#execute execute()} using the following inputs.
   * <P>
   * <TABLE border="1" cellpadding="8">
   * <TR align="left"><TH colspan=2> 4. execute (Success) </TH></TR>
   * <TR align="left"><TH> Input Choice </TH> <TH> Value </TH></TR>
   * <TR><TD> InputDefPatterns.Count </TD> <TD> None </TD> </TR>
   * <TR><TD> InputDefPatterns.Matched </TD> <TD> One </TD> </TR>
   * <TR><TD> InputDir </TD> <TD> Default </TD> </TR>
   * <TR><TD> OutDir </TD> <TD> Default </TD> </TR>
   * <TR><TD> OutFile.Defined </TD> <TD> Yes </TD> </TR>
   * <TR><TD> OutFile.Wildcard </TD> <TD> None </TD> </TR>
   * <TR><TD> TestDef.Defined </TD> <TD> NA </TD> </TR>
   * <TR><TD> TestDef.Wildcard </TD> <TD> NA </TD> </TR>
   * <TR><TD> TestDef.Exists </TD> <TD> NA </TD> </TR>
   * <TR><TD> GenDef.Defined </TD> <TD> Yes </TD> </TR>
   * <TR><TD> GenDef.Wildcard </TD> <TD> None </TD> </TR>
   * <TR><TD> GenDef.Exists </TD> <TD> Yes </TD> </TR>
   * <TR><TD> TransformDef.Defined </TD> <TD> Yes </TD> </TR>
   * <TR><TD> TransformDef.Relative </TD> <TD> Yes </TD> </TR>
   * <TR><TD> TransformDef.Wildcard </TD> <TD> None </TD> </TR>
   * <TR><TD> TransformDef.Exists </TD> <TD> Yes </TD> </TR>
   * <TR><TD> TransformParams.Defined </TD> <TD> No </TD> </TR>
   * <TR><TD> Junit </TD> <TD> No </TD> </TR>
   * <TR><TD> NewTests </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Seed.Defined </TD> <TD> Yes </TD> </TR>
   * <TR><TD> DefaultTupleSize.Defined </TD> <TD> No </TD> </TR>
   * </TABLE>
   * </P>
   */
  @Test
  public void execute_4()
    {
    // Given...

    // When...

    // Then...
    }

  /**
   * Tests {@link TcasesMojo#execute execute()} using the following inputs.
   * <P>
   * <TABLE border="1" cellpadding="8">
   * <TR align="left"><TH colspan=2> 5. execute (<FONT color="red">Failure</FONT>) </TH></TR>
   * <TR align="left"><TH> Input Choice </TH> <TH> Value </TH></TR>
   * <TR><TD> InputDefPatterns.Count </TD> <TD> One </TD> </TR>
   * <TR><TD> InputDefPatterns.Matched </TD> <TD> Many </TD> </TR>
   * <TR><TD> InputDir </TD> <TD> Default </TD> </TR>
   * <TR><TD> OutDir </TD> <TD> Default </TD> </TR>
   * <TR><TD> OutFile.Defined </TD> <TD> No </TD> </TR>
   * <TR><TD> OutFile.Wildcard </TD> <TD> NA </TD> </TR>
   * <TR><TD> TestDef.Defined </TD> <TD> Yes </TD> </TR>
   * <TR><TD> TestDef.Wildcard </TD> <TD> <FONT color="red"> Many  </FONT> </TD> </TR>
   * <TR><TD> TestDef.Exists </TD> <TD> Yes </TD> </TR>
   * <TR><TD> GenDef.Defined </TD> <TD> No </TD> </TR>
   * <TR><TD> GenDef.Wildcard </TD> <TD> NA </TD> </TR>
   * <TR><TD> GenDef.Exists </TD> <TD> NA </TD> </TR>
   * <TR><TD> TransformDef.Defined </TD> <TD> No </TD> </TR>
   * <TR><TD> TransformDef.Relative </TD> <TD> NA </TD> </TR>
   * <TR><TD> TransformDef.Wildcard </TD> <TD> NA </TD> </TR>
   * <TR><TD> TransformDef.Exists </TD> <TD> NA </TD> </TR>
   * <TR><TD> TransformParams.Defined </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Junit </TD> <TD> Yes </TD> </TR>
   * <TR><TD> NewTests </TD> <TD> Default </TD> </TR>
   * <TR><TD> Seed.Defined </TD> <TD> No </TD> </TR>
   * <TR><TD> DefaultTupleSize.Defined </TD> <TD> No </TD> </TR>
   * </TABLE>
   * </P>
   */
  @Test
  public void execute_5()
    {
    // Given...

    // When...

    // Then...
    }

  /**
   * Tests {@link TcasesMojo#execute execute()} using the following inputs.
   * <P>
   * <TABLE border="1" cellpadding="8">
   * <TR align="left"><TH colspan=2> 6. execute (<FONT color="red">Failure</FONT>) </TH></TR>
   * <TR align="left"><TH> Input Choice </TH> <TH> Value </TH></TR>
   * <TR><TD> InputDefPatterns.Count </TD> <TD> Many </TD> </TR>
   * <TR><TD> InputDefPatterns.Matched </TD> <TD> One </TD> </TR>
   * <TR><TD> InputDir </TD> <TD> Default </TD> </TR>
   * <TR><TD> OutDir </TD> <TD> Default </TD> </TR>
   * <TR><TD> OutFile.Defined </TD> <TD> Yes </TD> </TR>
   * <TR><TD> OutFile.Wildcard </TD> <TD> One </TD> </TR>
   * <TR><TD> TestDef.Defined </TD> <TD> Yes </TD> </TR>
   * <TR><TD> TestDef.Wildcard </TD> <TD> One </TD> </TR>
   * <TR><TD> TestDef.Exists </TD> <TD> Yes </TD> </TR>
   * <TR><TD> GenDef.Defined </TD> <TD> Yes </TD> </TR>
   * <TR><TD> GenDef.Wildcard </TD> <TD> One </TD> </TR>
   * <TR><TD> GenDef.Exists </TD> <TD> Yes </TD> </TR>
   * <TR><TD> TransformDef.Defined </TD> <TD> Yes </TD> </TR>
   * <TR><TD> TransformDef.Relative </TD> <TD> No </TD> </TR>
   * <TR><TD> TransformDef.Wildcard </TD> <TD> One </TD> </TR>
   * <TR><TD> TransformDef.Exists </TD> <TD> <FONT color="red"> No  </FONT> </TD> </TR>
   * <TR><TD> TransformParams.Defined </TD> <TD> No </TD> </TR>
   * <TR><TD> Junit </TD> <TD> Default </TD> </TR>
   * <TR><TD> NewTests </TD> <TD> No </TD> </TR>
   * <TR><TD> Seed.Defined </TD> <TD> Yes </TD> </TR>
   * <TR><TD> DefaultTupleSize.Defined </TD> <TD> No </TD> </TR>
   * </TABLE>
   * </P>
   */
  @Test
  public void execute_6()
    {
    // Given...

    // When...

    // Then...
    }

  /**
   * Tests {@link TcasesMojo#execute execute()} using the following inputs.
   * <P>
   * <TABLE border="1" cellpadding="8">
   * <TR align="left"><TH colspan=2> 7. execute (<FONT color="red">Failure</FONT>) </TH></TR>
   * <TR align="left"><TH> Input Choice </TH> <TH> Value </TH></TR>
   * <TR><TD> InputDefPatterns.Count </TD> <TD> None </TD> </TR>
   * <TR><TD> InputDefPatterns.Matched </TD> <TD> Many </TD> </TR>
   * <TR><TD> InputDir </TD> <TD> Default </TD> </TR>
   * <TR><TD> OutDir </TD> <TD> Default </TD> </TR>
   * <TR><TD> OutFile.Defined </TD> <TD> Yes </TD> </TR>
   * <TR><TD> OutFile.Wildcard </TD> <TD> None </TD> </TR>
   * <TR><TD> TestDef.Defined </TD> <TD> Yes </TD> </TR>
   * <TR><TD> TestDef.Wildcard </TD> <TD> None </TD> </TR>
   * <TR><TD> TestDef.Exists </TD> <TD> <FONT color="red"> No  </FONT> </TD> </TR>
   * <TR><TD> GenDef.Defined </TD> <TD> Yes </TD> </TR>
   * <TR><TD> GenDef.Wildcard </TD> <TD> None </TD> </TR>
   * <TR><TD> GenDef.Exists </TD> <TD> Yes </TD> </TR>
   * <TR><TD> TransformDef.Defined </TD> <TD> Yes </TD> </TR>
   * <TR><TD> TransformDef.Relative </TD> <TD> Yes </TD> </TR>
   * <TR><TD> TransformDef.Wildcard </TD> <TD> None </TD> </TR>
   * <TR><TD> TransformDef.Exists </TD> <TD> Yes </TD> </TR>
   * <TR><TD> TransformParams.Defined </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Junit </TD> <TD> No </TD> </TR>
   * <TR><TD> NewTests </TD> <TD> Default </TD> </TR>
   * <TR><TD> Seed.Defined </TD> <TD> No </TD> </TR>
   * <TR><TD> DefaultTupleSize.Defined </TD> <TD> No </TD> </TR>
   * </TABLE>
   * </P>
   */
  @Test
  public void execute_7()
    {
    // Given...

    // When...

    // Then...
    }

  /**
   * Tests {@link TcasesMojo#execute execute()} using the following inputs.
   * <P>
   * <TABLE border="1" cellpadding="8">
   * <TR align="left"><TH colspan=2> 8. execute (<FONT color="red">Failure</FONT>) </TH></TR>
   * <TR align="left"><TH> Input Choice </TH> <TH> Value </TH></TR>
   * <TR><TD> InputDefPatterns.Count </TD> <TD> One </TD> </TR>
   * <TR><TD> InputDefPatterns.Matched </TD> <TD> One </TD> </TR>
   * <TR><TD> InputDir </TD> <TD> Default </TD> </TR>
   * <TR><TD> OutDir </TD> <TD> Default </TD> </TR>
   * <TR><TD> OutFile.Defined </TD> <TD> No </TD> </TR>
   * <TR><TD> OutFile.Wildcard </TD> <TD> NA </TD> </TR>
   * <TR><TD> TestDef.Defined </TD> <TD> No </TD> </TR>
   * <TR><TD> TestDef.Wildcard </TD> <TD> NA </TD> </TR>
   * <TR><TD> TestDef.Exists </TD> <TD> NA </TD> </TR>
   * <TR><TD> GenDef.Defined </TD> <TD> Yes </TD> </TR>
   * <TR><TD> GenDef.Wildcard </TD> <TD> One </TD> </TR>
   * <TR><TD> GenDef.Exists </TD> <TD> <FONT color="red"> No  </FONT> </TD> </TR>
   * <TR><TD> TransformDef.Defined </TD> <TD> No </TD> </TR>
   * <TR><TD> TransformDef.Relative </TD> <TD> NA </TD> </TR>
   * <TR><TD> TransformDef.Wildcard </TD> <TD> NA </TD> </TR>
   * <TR><TD> TransformDef.Exists </TD> <TD> NA </TD> </TR>
   * <TR><TD> TransformParams.Defined </TD> <TD> No </TD> </TR>
   * <TR><TD> Junit </TD> <TD> Yes </TD> </TR>
   * <TR><TD> NewTests </TD> <TD> No </TD> </TR>
   * <TR><TD> Seed.Defined </TD> <TD> Yes </TD> </TR>
   * <TR><TD> DefaultTupleSize.Defined </TD> <TD> No </TD> </TR>
   * </TABLE>
   * </P>
   */
  @Test
  public void execute_8()
    {
    // Given...

    // When...

    // Then...
    }

  /**
   * Tests {@link TcasesMojo#execute execute()} using the following inputs.
   * <P>
   * <TABLE border="1" cellpadding="8">
   * <TR align="left"><TH colspan=2> 9. execute (<FONT color="red">Failure</FONT>) </TH></TR>
   * <TR align="left"><TH> Input Choice </TH> <TH> Value </TH></TR>
   * <TR><TD> InputDefPatterns.Count </TD> <TD> Many </TD> </TR>
   * <TR><TD> InputDefPatterns.Matched </TD> <TD> Many </TD> </TR>
   * <TR><TD> InputDir </TD> <TD> Default </TD> </TR>
   * <TR><TD> OutDir </TD> <TD> Default </TD> </TR>
   * <TR><TD> OutFile.Defined </TD> <TD> Yes </TD> </TR>
   * <TR><TD> OutFile.Wildcard </TD> <TD> <FONT color="red"> Many  </FONT> </TD> </TR>
   * <TR><TD> TestDef.Defined </TD> <TD> NA </TD> </TR>
   * <TR><TD> TestDef.Wildcard </TD> <TD> NA </TD> </TR>
   * <TR><TD> TestDef.Exists </TD> <TD> NA </TD> </TR>
   * <TR><TD> GenDef.Defined </TD> <TD> No </TD> </TR>
   * <TR><TD> GenDef.Wildcard </TD> <TD> NA </TD> </TR>
   * <TR><TD> GenDef.Exists </TD> <TD> NA </TD> </TR>
   * <TR><TD> TransformDef.Defined </TD> <TD> Yes </TD> </TR>
   * <TR><TD> TransformDef.Relative </TD> <TD> No </TD> </TR>
   * <TR><TD> TransformDef.Wildcard </TD> <TD> One </TD> </TR>
   * <TR><TD> TransformDef.Exists </TD> <TD> Yes </TD> </TR>
   * <TR><TD> TransformParams.Defined </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Junit </TD> <TD> Default </TD> </TR>
   * <TR><TD> NewTests </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Seed.Defined </TD> <TD> No </TD> </TR>
   * <TR><TD> DefaultTupleSize.Defined </TD> <TD> No </TD> </TR>
   * </TABLE>
   * </P>
   */
  @Test
  public void execute_9()
    {
    // Given...

    // When...

    // Then...
    }

  /**
   * Tests {@link TcasesMojo#execute execute()} using the following inputs.
   * <P>
   * <TABLE border="1" cellpadding="8">
   * <TR align="left"><TH colspan=2> 10. execute (<FONT color="red">Failure</FONT>) </TH></TR>
   * <TR align="left"><TH> Input Choice </TH> <TH> Value </TH></TR>
   * <TR><TD> InputDefPatterns.Count </TD> <TD> None </TD> </TR>
   * <TR><TD> InputDefPatterns.Matched </TD> <TD> One </TD> </TR>
   * <TR><TD> InputDir </TD> <TD> Default </TD> </TR>
   * <TR><TD> OutDir </TD> <TD> Default </TD> </TR>
   * <TR><TD> OutFile.Defined </TD> <TD> Yes </TD> </TR>
   * <TR><TD> OutFile.Wildcard </TD> <TD> One </TD> </TR>
   * <TR><TD> TestDef.Defined </TD> <TD> Yes </TD> </TR>
   * <TR><TD> TestDef.Wildcard </TD> <TD> One </TD> </TR>
   * <TR><TD> TestDef.Exists </TD> <TD> Yes </TD> </TR>
   * <TR><TD> GenDef.Defined </TD> <TD> Yes </TD> </TR>
   * <TR><TD> GenDef.Wildcard </TD> <TD> <FONT color="red"> Many  </FONT> </TD> </TR>
   * <TR><TD> GenDef.Exists </TD> <TD> Yes </TD> </TR>
   * <TR><TD> TransformDef.Defined </TD> <TD> Yes </TD> </TR>
   * <TR><TD> TransformDef.Relative </TD> <TD> Yes </TD> </TR>
   * <TR><TD> TransformDef.Wildcard </TD> <TD> None </TD> </TR>
   * <TR><TD> TransformDef.Exists </TD> <TD> Yes </TD> </TR>
   * <TR><TD> TransformParams.Defined </TD> <TD> No </TD> </TR>
   * <TR><TD> Junit </TD> <TD> No </TD> </TR>
   * <TR><TD> NewTests </TD> <TD> Default </TD> </TR>
   * <TR><TD> Seed.Defined </TD> <TD> Yes </TD> </TR>
   * <TR><TD> DefaultTupleSize.Defined </TD> <TD> No </TD> </TR>
   * </TABLE>
   * </P>
   */
  @Test
  public void execute_10()
    {
    // Given...

    // When...

    // Then...
    }

  /**
   * Tests {@link TcasesMojo#execute execute()} using the following inputs.
   * <P>
   * <TABLE border="1" cellpadding="8">
   * <TR align="left"><TH colspan=2> 11. execute (<FONT color="red">Failure</FONT>) </TH></TR>
   * <TR align="left"><TH> Input Choice </TH> <TH> Value </TH></TR>
   * <TR><TD> InputDefPatterns.Count </TD> <TD> One </TD> </TR>
   * <TR><TD> InputDefPatterns.Matched </TD> <TD> Many </TD> </TR>
   * <TR><TD> InputDir </TD> <TD> Default </TD> </TR>
   * <TR><TD> OutDir </TD> <TD> Default </TD> </TR>
   * <TR><TD> OutFile.Defined </TD> <TD> Yes </TD> </TR>
   * <TR><TD> OutFile.Wildcard </TD> <TD> None </TD> </TR>
   * <TR><TD> TestDef.Defined </TD> <TD> Yes </TD> </TR>
   * <TR><TD> TestDef.Wildcard </TD> <TD> None </TD> </TR>
   * <TR><TD> TestDef.Exists </TD> <TD> Yes </TD> </TR>
   * <TR><TD> GenDef.Defined </TD> <TD> Yes </TD> </TR>
   * <TR><TD> GenDef.Wildcard </TD> <TD> None </TD> </TR>
   * <TR><TD> GenDef.Exists </TD> <TD> Yes </TD> </TR>
   * <TR><TD> TransformDef.Defined </TD> <TD> Yes </TD> </TR>
   * <TR><TD> TransformDef.Relative </TD> <TD> No </TD> </TR>
   * <TR><TD> TransformDef.Wildcard </TD> <TD> <FONT color="red"> Many  </FONT> </TD> </TR>
   * <TR><TD> TransformDef.Exists </TD> <TD> Yes </TD> </TR>
   * <TR><TD> TransformParams.Defined </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Junit </TD> <TD> Default </TD> </TR>
   * <TR><TD> NewTests </TD> <TD> No </TD> </TR>
   * <TR><TD> Seed.Defined </TD> <TD> No </TD> </TR>
   * <TR><TD> DefaultTupleSize.Defined </TD> <TD> No </TD> </TR>
   * </TABLE>
   * </P>
   */
  @Test
  public void execute_11()
    {
    // Given...

    // When...

    // Then...
    }

  /**
   * Returns the set of paths relative to the given base directory matching any of the given patterns.
   */
  private String[] findPathsMatching( File baseDir, String... patterns)
    {
    DirectoryScanner scanner = new DirectoryScanner();
    scanner.setBasedir( baseDir);
    scanner.setIncludes( patterns);
    scanner.scan();
    return scanner.getIncludedFiles();
    }

  /**
   * Returns the path to the base directory for the given test project.
   */
  private File getBaseDirTest( String testProjectName)
    {
    return new File( getTestProjectDir(), testProjectName);
    }

  /**
   * Returns the path to the directory containing test projects.
   */
  private File getTestProjectDir()
    {
    return new File( PlexusTestCase.getBasedir(), "src/test/resources");
    }

  @Rule
  public MojoRule mojoHelper = new MojoRule();
  }
