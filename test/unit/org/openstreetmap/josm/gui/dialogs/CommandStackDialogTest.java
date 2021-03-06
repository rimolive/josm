// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.gui.dialogs;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.openstreetmap.josm.TestUtils;
import org.openstreetmap.josm.command.Command;
import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.MapFrame;
import org.openstreetmap.josm.gui.layer.OsmDataLayer;
import org.openstreetmap.josm.testutils.JOSMTestRules;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Unit tests of {@link CommandStackDialog} class.
 */
public class CommandStackDialogTest {

    /**
     * Setup tests
     */
    @Rule
    @SuppressFBWarnings(value = "URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD")
    public JOSMTestRules test = new JOSMTestRules().main().platform().projection();

    /**
     * Unit test of {@link CommandStackDialog} class - empty case.
     */
    @Test
    public void testCommandStackDialogEmpty() {
        CommandStackDialog dlg = new CommandStackDialog();
        dlg.showDialog();
        assertTrue(dlg.isVisible());
        dlg.hideDialog();
        assertFalse(dlg.isVisible());
    }

    /**
     * Unit test of {@link CommandStackDialog} class - not empty case.
     */
    @Test
    public void testCommandStackDialogNotEmpty() {
        DataSet ds = new DataSet();
        OsmDataLayer layer = new OsmDataLayer(ds, "", null);
        MainApplication.getLayerManager().addLayer(layer);
        try {
            Command cmd1 = TestUtils.newCommand(ds);
            Command cmd2 = TestUtils.newCommand(ds);
            MainApplication.undoRedo.add(cmd1);
            MainApplication.undoRedo.add(cmd2);
            MainApplication.undoRedo.undo(1);

            assertFalse(MainApplication.undoRedo.commands.isEmpty());
            assertFalse(MainApplication.undoRedo.redoCommands.isEmpty());

            MapFrame map = MainApplication.getMap();
            CommandStackDialog dlg = new CommandStackDialog();
            map.addToggleDialog(dlg);
            dlg.unfurlDialog();
            assertTrue(dlg.isVisible());
            map.removeToggleDialog(dlg);
            dlg.hideDialog();
            assertFalse(dlg.isVisible());
        } finally {
            MainApplication.undoRedo.clean();
            MainApplication.getLayerManager().removeLayer(layer);
        }
    }
}
