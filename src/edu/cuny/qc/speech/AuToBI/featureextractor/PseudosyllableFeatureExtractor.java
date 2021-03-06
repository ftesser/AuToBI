/*  PseudosyllableFeatureExtractor.java

    Copyright (c) 2009-2010 Andrew Rosenberg
    
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
package edu.cuny.qc.speech.AuToBI.featureextractor;

import edu.cuny.qc.speech.AuToBI.core.Region;
import edu.cuny.qc.speech.AuToBI.core.FeatureExtractor;
import edu.cuny.qc.speech.AuToBI.core.WavData;
import edu.cuny.qc.speech.AuToBI.util.SubregionUtils;
import edu.cuny.qc.speech.AuToBI.Syllabifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * PseudosyllableFeatureExtractor hypothesizes syllables for a given signal and aligns the loudest contained syllable to
 * each region.
 * <p/>
 * These pseudosyllables are generated by Syllabifier, and are stored as SubWord features on the regions.
 *
 * @see edu.cuny.qc.speech.AuToBI.Syllabifier
 * @see edu.cuny.qc.speech.AuToBI.core.SubWord
 */
@SuppressWarnings("unchecked")
public class PseudosyllableFeatureExtractor extends FeatureExtractor {
  private String subregion_name;  // the feature name to store the subregion

  /**
   * Constructs a new PseudosyllableFeatureExtractor.
   *
   * @param subregion_name the feature name to store the subregion
   */
  public PseudosyllableFeatureExtractor(String subregion_name) {
    super();
    this.subregion_name = subregion_name;
    this.required_features.add("wav");
    this.extracted_features.add(subregion_name);
  }

  /**
   * Identifies the loudest pseudosyllable for a given region, constructs a SubWord object, and stores it with each
   * region.
   *
   * @param regions The regions to extract features from.
   * @throws edu.cuny.qc.speech.AuToBI.featureextractor.FeatureExtractorException
   *          if anything goes wrong.
   */
  public void extractFeatures(List regions) throws FeatureExtractorException {
    // Identify all regions which are associated with each wav data.
    HashMap<WavData, List<Region>> wave_region_map = new HashMap<WavData, List<Region>>();
    for (Region r : (List<Region>) regions) {
      WavData wav = (WavData) r.getAttribute("wav");
      if (wav != null) {
        if (!wave_region_map.containsKey(wav)) {
          wave_region_map.put(wav, new ArrayList<Region>());
        }
        wave_region_map.get(wav).add(r);
      }
    }

    for (WavData wav : wave_region_map.keySet()) {
      Syllabifier syllabifier = new Syllabifier();
      List<Region> pseudosyllables = syllabifier.generatePseudosyllableRegions(wav);

      SubregionUtils.alignLongestSubregionsToWords(regions, pseudosyllables, subregion_name);
    }

  }
}
