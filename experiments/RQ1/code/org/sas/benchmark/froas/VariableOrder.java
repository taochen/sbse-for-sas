package org.sas.benchmark.froas;

import java.util.ArrayList;
import java.util.List;

public class VariableOrder {

	private static List<String> APACHE = new ArrayList<String>();
	private static List<String> BDBJ = new ArrayList<String>();
	private static List<String> BDBC = new ArrayList<String>();
	private static List<String> LLVM = new ArrayList<String>();
	private static List<String> SQL = new ArrayList<String>();
	private static List<String> X264 = new ArrayList<String>();
	
	static {
		// The excluded ones are still here but they do not affect the order
		String[] array = new String[]{
				"Base",
				"HostnameLookups",
				"KeepAlive",
				"EnableSendfile",
				"FollowSymLinks",
				"AccessLog",
				"ExtendedStatus",
				"InMemory",
				"Handle"
				
		};
		
		attach(APACHE, array);
		
		array = new String[]{
				"Base",
				"Persistence",
				"IO",
				"OldIO",
				"NewIO",
				"NIOBase",
				"NIOType",
				"ChunkedNIO",
				"SingleWriteNIO",
				"DirectNIO",
				"LogSize",
				"S100MiB",
				"S1MiB",
				"Checksum",
				"BTreeFeatures",
				"INCompressor",
				"IEvictor",
				"Evictor",
				"Critical_Eviction",
				"Verifier",
				"ITracing",
				"Tracing",
				"TracingLevel",
				"Severe",
				"Finest",
				"Statistics"
				
		};
		
		attach(BDBJ, array);
		
		array = new String[]{
				"HAVE_CRYPTO",
				"HAVE_HASH",
				"HAVE_REPLICATION",
				"HAVE_VERIFY",
				"HAVE_SEQUENCE",
				"HAVE_STATISTICS",
				"DIAGNOSTIC",
				"PAGESIZE",
				"PS1K",
				"PS4K",
				"PS8K",
				"PS16K",
				"PS32K",
				"CACHESIZE",
				"CS32MB",
				"CS16MB",
				"CS64MB",
				"CS512MB"
				
		};
		
		attach(BDBC, array);
		
		array = new String[]{
				"time_passes",
				"gvn",
				"instcombine",
				"inline",
				"jump_threading",
				"simplifycfg",
				"sccp",
				"print_used_types",
				"ipsccp",
				"iv_users",
				"licm"
				
		};
		
		attach(LLVM, array);
		
		
		array = new String[]{
				"OperatingSystemCharacteristics",
				"SQLITE_SECURE_DELETE",
				"ChooseSQLITE_TEMP_STORE",
				"SQLITE_TEMP_STOREzero",
				"SQLITE_TEMP_STOREone",
				"SQLITE_TEMP_STOREtwo",
				"SQLITE_TEMP_STOREthree",
				"EnableFeatures",
				"SQLITE_ENABLE_ATOMIC_WRITE",
				"SQLITE_ENABLE_STAT2",
				"DisableFeatures",
				"SQLITE_DISABLE_LFS",
				"SQLITE_DISABLE_DIRSYNC",
				"OmitFeatures",
				"SQLITE_OMIT_AUTOMATIC_INDEX",
				"SQLITE_OMIT_BETWEEN_OPTIMIZATION",
				"SQLITE_OMIT_BTREECOUNT",
				"SQLITE_OMIT_LIKE_OPTIMIZATION",
				"SQLITE_OMIT_LOOKASIDE",
				"SQLITE_OMIT_OR_OPTIMIZATION",
				"SQLITE_OMIT_QUICKBALANCE",
				"SQLITE_OMIT_SHARED_CACHE",
				"SQLITE_OMIT_XFER_OPT",
				"Options",
				"SetAutoVacuum",
				"AutoVacuumOff",
				"AutoVacuumOn",
				"SetCacheSize",
				"StandardCacheSize",
				"LowerCacheSize",
				"HigherCacheSize",
				"LockingMode",
				"ExclusiveLock",
				"NormalLockingMode",
				"PageSize",
				"StandardPageSize",
				"LowerPageSize",
				"HigherPageSize",
				"HighestPageSize"
				
		};
		
		attach(SQL, array);
		
		array = new String[]{
				"no_asm",
				"no_8x8dct",
				"no_cabac",
				"no_deblock",
				"no_fast_pskip",
				"no_mbtree",
				"no_mixed_refs",
				"no_weightb",
				"rc_lookahead",
				"rc_lookahead_20",
				"rc_lookahead_40",
				"rc_lookahead_60",
				"ref",
				"ref_1",
				"ref_5",
				"ref_9"
				
		};
		
		attach(X264, array);
	}
	
	public static List<String> getList(){
		if("APACHEAll".equals(Parser.selected)) {
			return APACHE;
		} else if("BDBCAll".equals(Parser.selected)) {
			return BDBC;
		} else if("BDBJAll".equals(Parser.selected)) {
			return BDBJ;
		} else if("LLVMAll".equals(Parser.selected)) {
			return LLVM;
		} else if("SQLAll".equals(Parser.selected)) {
			return SQL;
		} else if("X264All".equals(Parser.selected)) {
			return X264;
		}
			
		return null;
	}
	
	private static void attach(List<String> list, String[] array){
		for (int i = 0; i < array.length; i++) {
			list.add(array[i]);
		}
		
	}
	
	public static void main(String[] arg) {
		for (int i = 0; i < BDBC.size(); i++) {
			//System.out.print(" <feature name=\""+X264.get(i)+"\" type=\"categorical\" optional=\"true\"/>\n");
			System.out.print("<item name=\""+BDBC.get(i)+"\" provision=\"0\" constraint=\"-1\" differences=\"1\" pre_to_max=\"0.7\" pre_of_max=\"0.1\" min=\"0\" max=\"1\" price_per_unit=\"0.5\" switchoff=\"true\" />\n");
		}
	}
}
