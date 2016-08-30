/*
 * This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License, version 2 as published by the Free Software
 * Foundation.
 *
 * You should have received a copy of the GNU General Public License along with this
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/gpl-2.0.html
 * or from the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 *
 * Copyright 2006 - 2016 Pentaho Corporation.  All rights reserved.
 */

package org.pentaho.platform.plugin.services.importexport;

import org.junit.Assert;
import org.mockito.Mockito;

import org.junit.Before;
import org.junit.Test;
import org.pentaho.platform.api.repository2.unified.RepositoryFile;
import org.pentaho.platform.plugin.services.importexport.exportManifest.ExportManifest;
import org.pentaho.platform.plugin.services.importexport.exportManifest.ExportManifestEntity;

public class ImportSessionTest {
  private static final String PATH = "path";

  private ImportSession importSession;

  @Before
  public void setUp() {
    importSession = ImportSession.getSession();
    importSession.initialize();
  }

  @Test
  public void testSettingAclProperties() {
    importSession.setAclProperties( true, true, true );
    Assert.assertTrue( importSession.isApplyAclSettings() );
    Assert.assertTrue( importSession.isRetainOwnership() );
    Assert.assertTrue( importSession.isOverwriteAclSettings() );

    importSession.setAclProperties( false, true, true );
    Assert.assertFalse( importSession.isApplyAclSettings() );
    Assert.assertTrue( importSession.isRetainOwnership() );
    Assert.assertTrue( importSession.isOverwriteAclSettings() );

    importSession.setAclProperties( false, false, true );
    Assert.assertFalse( importSession.isApplyAclSettings() );
    Assert.assertFalse( importSession.isRetainOwnership() );
    Assert.assertTrue( importSession.isOverwriteAclSettings() );

    importSession.setAclProperties( false, false, false );
    Assert.assertFalse( importSession.isApplyAclSettings() );
    Assert.assertFalse( importSession.isRetainOwnership() );
    Assert.assertFalse( importSession.isOverwriteAclSettings() );

    importSession.setAclProperties( true, false, false );
    Assert.assertTrue( importSession.isApplyAclSettings() );
    Assert.assertFalse( importSession.isRetainOwnership() );
    Assert.assertFalse( importSession.isOverwriteAclSettings() );

    importSession.setAclProperties( true, true, false );
    Assert.assertTrue( importSession.isApplyAclSettings() );
    Assert.assertTrue( importSession.isRetainOwnership() );
    Assert.assertFalse( importSession.isOverwriteAclSettings() );
  }

  @Test
  public void fileHiddenPropertyIsNullWhenAclIsDisabled() {
    importSession.setAclProperties( false, false, false );
    Assert.assertNull( importSession.isFileHidden( PATH ) );
  }

  @Test
  public void fileHiddenPropertyIsDefinedWhenAclIsEnabled() {
    importSession.setAclProperties( true, false, false );

    RepositoryFile virtualFile = Mockito.mock( RepositoryFile.class );
    Mockito.when( virtualFile.isHidden() ).thenReturn( Boolean.TRUE );

    ExportManifestEntity fake = Mockito.mock( ExportManifestEntity.class );
    Mockito.when( fake.getRepositoryFile() ).thenReturn( virtualFile );

    ExportManifest manifest = Mockito.mock( ExportManifest.class );
    Mockito.when( manifest.getExportManifestEntity( PATH ) ).thenReturn( fake );

    importSession.setManifest( manifest );
    Assert.assertEquals( virtualFile.isHidden(), importSession.isFileHidden( PATH ) );
  }

  @Test
  public void testClearSession() {
    ExportManifest manifest = Mockito.mock( ExportManifest.class );
    ImportSession.getSession().setManifest( manifest );
    Assert.assertNotNull( ImportSession.getSession().getManifest() );
    ImportSession.clearSession();
    Assert.assertNull( ImportSession.getSession().getManifest() );
  }
}
