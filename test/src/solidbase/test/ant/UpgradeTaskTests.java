package solidbase.test.ant;

import org.apache.tools.ant.BuildFileTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import solidbase.test.TestUtil;

public class UpgradeTaskTests extends BuildFileTest
{
	@Test
	public void testUpgradeTask()
	{
		configureProject( "test-upgradetask.xml" );
		executeTarget( "ant-test" );
		String log = TestUtil.generalizeOutput( getLog() );
		Assert.assertEquals( log, "SolidBase v1.5.x(C) 2006-2009 Rene M. de BlooisConnecting to database...The database has no version yet.Opening patchfile 'file:/.../testpatch-multiconnections.sql'    Encoding is 'ISO-8859-1'Patching to \"1.0.1\"    Creating table DBVERSION.    Creating table DBVERSIONLOG.Patching \"1.0.1\" to \"1.0.2\".    Inserting admin user.The database has been patched.Current database version is \"1.0.2\"." );
	}

	@Test(groups={"new"})
	public void testUpgradeTaskBaseDir()
	{
		configureProject( "test-upgradetask.xml" );
		executeTarget( "ant-basedir-test" );
		String log = TestUtil.generalizeOutput( getLog() );
		Assert.assertEquals( log, "SolidBase v1.5.x(C) 2006-2009 Rene M. de BlooisConnecting to database...The database has no version yet.Opening patchfile 'C:\\...\\testpatch-basedir.sql'    Encoding is 'ISO-8859-1'Patching to \"1.0.1\"    Creating table DBVERSION.    Creating table DBVERSIONLOG.Patching \"1.0.1\" to \"1.0.2\"    Creating table USERS.    Inserting admin user.The database has been patched.Current database version is \"1.0.2\"." );
	}
}
