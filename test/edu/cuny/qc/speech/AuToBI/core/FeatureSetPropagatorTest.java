/*  FeatureSetPropagatorTest.java

    Copyright (c) 2011 Andrew Rosenberg

    This file is part of the AuToBI prosodic analysis package.

    AuToBI is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    AuToBI is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with AuToBI.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.cuny.qc.speech.AuToBI.core;

import edu.cuny.qc.speech.AuToBI.AuToBI;
import edu.cuny.qc.speech.AuToBI.io.FormattedFile;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.*;

/**
 * Test class for FeatureSetPropagator.
 * <p/>
 * These tests are much larger than the standard UnitTests.
 * <p/>
 * FeatureSetPropagator is responsible for reading word segmentation, and a wave file, then calling the feature
 * extraction routines to generate the required features for a FeatureSet.
 * <p/>
 * These test confirm that the process correctly operates on each style of input segmentation, generating appropriately
 * FeatureSet objects.
 * <p/>
 * Specific testing of file readers and feature extractors is reserved to their corresponding unit tests.
 *
 * @see edu.cuny.qc.speech.AuToBI.core.FeatureSetPropagator
 */
public class FeatureSetPropagatorTest {

  final String TEST_DIR = "/Users/andrew/code/AuToBI/release/test_data";

  @Test
  public void testConstructor() {

    AuToBI autobi = new AuToBI();
    FormattedFile file = new FormattedFile(TEST_DIR + "/no_file.txt");
    FeatureSet fs = new FeatureSet();

    FeatureSetPropagator fsp = new FeatureSetPropagator(autobi, file, fs);
  }

  @Test
  public void testPropagateTextGridData() {
    AuToBI autobi = new AuToBI();
    FormattedFile file = new FormattedFile(TEST_DIR + "/test.TextGrid");
    FeatureSet fs = new FeatureSet();

    FeatureSetPropagator fsp = new FeatureSetPropagator(autobi, file, fs);
    FeatureSet new_fs = fsp.call();

    assertFalse(fs == new_fs);
    assertTrue(new_fs.getDataPoints().size() > 0);
  }

  @Test
  public void testPropagateSimpleWordData() {
    AuToBI autobi = new AuToBI();
    FormattedFile file = new FormattedFile(TEST_DIR + "/test.txt", FormattedFile.Format.SIMPLE_WORD);
    FeatureSet fs = new FeatureSet();

    FeatureSetPropagator fsp = new FeatureSetPropagator(autobi, file, fs);
    FeatureSet new_fs = fsp.call();
    assertFalse(fs == new_fs);
    assertTrue(new_fs.getDataPoints().size() > 0);
  }
}
