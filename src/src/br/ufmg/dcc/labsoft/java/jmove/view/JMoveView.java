package br.ufmg.dcc.labsoft.java.jmove.view;

//import gr.uom.java.ast.CompilationUnitCache;
//import gr.uom.java.distance.MoveMethodCandidateRefactoring;
//import gr.uom.java.jdeodorant.preferences.PreferenceConstants;
//import gr.uom.java.jdeodorant.refactoring.Activator;
//import gr.uom.java.jdeodorant.refactoring.views.FeatureEnvy.MyToolTip;

import gr.uom.java.jdeodorant.refactoring.manipulators.MoveMethodRefactoring;
import gr.uom.java.jdeodorant.refactoring.views.MyRefactoringWizard;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.AnnotationModel;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.ui.refactoring.RefactoringWizardOpenOperation;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.texteditor.ITextEditor;

import br.ufmg.dcc.labsoft.java.jmove.principal.Main;
import br.ufmg.dcc.labsoft.java.jmove.suggestion.Suggestion;
import br.ufmg.dcc.labsoft.java.jmove.utils.CandidateMap;

public class JMoveView extends ViewPart {
	private TableViewer tableViewer;
	// private CandidateRefactoring[] candidateRefactoringTable;
	private IJavaProject selectedProject;
	private Action applyRefactoringAction;
	private Action identifyBadSmellsAction;
	private Action doubleClickAction;
	private Action saveResultsAction;
	private CandidateMap map;

	@Override
	public void createPartControl(Composite parent) {
		tableViewer = new TableViewer(parent, SWT.SINGLE | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
		tableViewer.setContentProvider(new ViewContentProvider());
		tableViewer.setLabelProvider(new ViewLabelProvider());
		tableViewer.setInput(getViewSite());
		TableLayout layout = new TableLayout();
		layout.addColumnData(new ColumnWeightData(40, true));
		layout.addColumnData(new ColumnWeightData(60, true));
		layout.addColumnData(new ColumnWeightData(40, true));
		layout.addColumnData(new ColumnWeightData(40, true));
		layout.addColumnData(new ColumnWeightData(20, true));
		tableViewer.getTable().setLayout(layout);
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.getTable().setHeaderVisible(true);
		TableColumn column0 = new TableColumn(tableViewer.getTable(), SWT.LEFT);
		column0.setText("Refactoring Type");
		column0.setResizable(true);
		column0.pack();
		TableColumn column1 = new TableColumn(tableViewer.getTable(), SWT.LEFT);
		column1.setText("Source Method");
		column1.setResizable(true);
		column1.pack();
		TableColumn column2 = new TableColumn(tableViewer.getTable(), SWT.LEFT);
		column2.setText("Target Class");
		column2.setResizable(true);
		column2.pack();

		tableViewer.setColumnProperties(new String[] { "type", "source",
				"target" });

		// tableViewer.setCellModifier(new ICellModifier() {
		// public boolean canModify(Object element, String property) {
		// return property.equals("rate");
		// }
		//
		// public void modify(Object element, String property, Object value) {
		// TableItem item = (TableItem) element;
		// Object data = item.getData();
		// if (data instanceof MoveMethodCandidateRefactoring) {
		// MoveMethodCandidateRefactoring candidate =
		// (MoveMethodCandidateRefactoring) data;
		// IPreferenceStore store = Activator.getDefault()
		// .getPreferenceStore();
		// boolean allowUsageReporting = store
		// .getBoolean(PreferenceConstants.P_ENABLE_USAGE_REPORTING);
		// if (allowUsageReporting) {
		// Table table = tableViewer.getTable();
		// int rankingPosition = -1;
		// for (int i = 0; i < table.getItemCount(); i++) {
		// TableItem tableItem = table.getItem(i);
		// if (tableItem.equals(item)) {
		// rankingPosition = i;
		// break;
		// }
		// }
		// try {
		// boolean allowSourceCodeReporting = store
		// .getBoolean(PreferenceConstants.P_ENABLE_SOURCE_CODE_REPORTING);
		// String declaringClass = candidate
		// .getSourceClassTypeDeclaration()
		// .resolveBinding().getQualifiedName();
		// String methodName = candidate
		// .getSourceMethodDeclaration()
		// .resolveBinding().toString();
		// String sourceMethodName = declaringClass + "::"
		// + methodName;
		// String content = URLEncoder.encode("project_name",
		// "UTF-8")
		// + "="
		// + URLEncoder.encode(
		// selectedProject.getElementName(),
		// "UTF-8");
		// content += "&"
		// + URLEncoder.encode("source_method_name",
		// "UTF-8")
		// + "="
		// + URLEncoder.encode(sourceMethodName,
		// "UTF-8");
		// content += "&"
		// + URLEncoder.encode("target_class_name",
		// "UTF-8")
		// + "="
		// + URLEncoder.encode(candidate.getTarget(),
		// "UTF-8");
		// content += "&"
		// + URLEncoder.encode("ranking_position",
		// "UTF-8")
		// + "="
		// + URLEncoder.encode(
		// String.valueOf(rankingPosition),
		// "UTF-8");
		// content += "&"
		// + URLEncoder.encode("total_opportunities",
		// "UTF-8")
		// + "="
		// + URLEncoder.encode(String.valueOf(table
		// .getItemCount() - 1), "UTF-8");
		// content += "&"
		// + URLEncoder.encode("EP", "UTF-8")
		// + "="
		// + URLEncoder.encode(String
		// .valueOf(candidate
		// .getEntityPlacement()),
		// "UTF-8");
		// content += "&"
		// + URLEncoder.encode("envied_elements",
		// "UTF-8")
		// + "="
		// + URLEncoder.encode(
		// String.valueOf(candidate
		// .getNumberOfDistinctEnviedElements()),
		// "UTF-8");
		// if (allowSourceCodeReporting)
		// content += "&"
		// + URLEncoder.encode(
		// "source_method_code", "UTF-8")
		// + "="
		// + URLEncoder.encode(candidate
		// .getSourceMethodDeclaration()
		// .toString(), "UTF-8");
		// content += "&"
		// + URLEncoder.encode("username", "UTF-8")
		// + "="
		// + URLEncoder.encode(
		// System.getProperty("user.name"),
		// "UTF-8");
		// content += "&" + URLEncoder.encode("tb", "UTF-8")
		// + "=" + URLEncoder.encode("0", "UTF-8");
		// URL url = new URL(Activator.RANK_URL);
		// URLConnection urlConn = url.openConnection();
		// urlConn.setDoInput(true);
		// urlConn.setDoOutput(true);
		// urlConn.setUseCaches(false);
		// urlConn.setRequestProperty("Content-Type",
		// "application/x-www-form-urlencoded");
		// DataOutputStream printout = new DataOutputStream(
		// urlConn.getOutputStream());
		// printout.writeBytes(content);
		// printout.flush();
		// printout.close();
		// DataInputStream input = new DataInputStream(urlConn
		// .getInputStream());
		// input.close();
		// } catch (IOException ioe) {
		// ioe.printStackTrace();
		// }
		// }
		// tableViewer.update(data, null);
		// }
		// }
		//
		// public Object getValue(Object arg0, String arg1) {
		// // TODO Auto-generated method stub
		// return null;
		// }
		// });

		makeActions();
		hookDoubleClickAction();
		contributeToActionBars();
		getSite().getWorkbenchWindow().getSelectionService()
				.addSelectionListener(selectionListener);
		// JavaCore.addElementChangedListener(new ElementChangedListener());
		// getSite().getWorkbenchWindow().getWorkbench().getOperationSupport()
		// .getOperationHistory()
		// .addOperationHistoryListener(new IOperationHistoryListener() {
		// public void historyNotification(OperationHistoryEvent event) {
		// int eventType = event.getEventType();
		// if (eventType == OperationHistoryEvent.UNDONE
		// || eventType == OperationHistoryEvent.REDONE
		// || eventType == OperationHistoryEvent.OPERATION_ADDED
		// || eventType == OperationHistoryEvent.OPERATION_REMOVED) {
		// applyRefactoringAction.setEnabled(false);
		// saveResultsAction.setEnabled(false);
		// evolutionAnalysisAction.setEnabled(false);
		// }
		// }
		// });

		// JFaceResources.getFontRegistry()
		// .put(MyToolTip.HEADER_FONT,
		// JFaceResources
		// .getFontRegistry()
		// .getBold(
		// JFaceResources.getDefaultFont()
		// .getFontData()[0].getName())
		// .getFontData());
		// MyToolTip toolTip = new MyToolTip(tableViewer.getControl());
		// toolTip.setShift(new Point(-5, -5));
		// toolTip.setHideOnMouseDown(false);
		// toolTip.activate();

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		tableViewer.getControl().setFocus();
	}

	private ISelectionListener selectionListener = new ISelectionListener() {
		public void selectionChanged(IWorkbenchPart sourcepart,
				ISelection selection) {
			System.out.println("Projeto chamou");
			if (selection instanceof IStructuredSelection) {
				IStructuredSelection structuredSelection = (IStructuredSelection) selection;
				Object element = structuredSelection.getFirstElement();
				IJavaProject javaProject = null;

				if (element instanceof IJavaProject) {
					javaProject = (IJavaProject) element;
					selectedProject = javaProject;
					identifyBadSmellsAction.setEnabled(true);
					saveResultsAction.setEnabled(false);
				}

				System.out.println("Aqui " + selectedProject.getElementName());
			}
		}
	};

	private void hookDoubleClickAction() {
		tableViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(identifyBadSmellsAction);
		manager.add(applyRefactoringAction);
		manager.add(saveResultsAction);
	}


	
	class ViewContentProvider implements IStructuredContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}

		public Object[] getElements(Object parent) {

			if (map != null) {
				return map.getCandidates();
			} else {
				return new CandidateMap[0];
			}
		}
	}

	class ViewLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		public String getColumnText(Object obj, int index) {

			Suggestion sug = (Suggestion) obj;

			switch (index) {
			case 0:
				return "Move Method";
			case 1:
				return sug.getMethodSignature();
			case 2:
				return sug.getClassName();
			default:
				return "";
			}

		}

		public Image getColumnImage(Object obj, int index) {
			return null;
		}

		public Image getImage(Object obj) {
			return null;
		}

	}

	private void makeActions() {
		identifyBadSmellsAction = new Action() {
			public void run() {
				// CompilationUnitCache.getInstance().clearCache();
				map = getTable();
				tableViewer.setContentProvider(new ViewContentProvider());
				applyRefactoringAction.setEnabled(true);
				saveResultsAction.setEnabled(true);

			}
		};
		identifyBadSmellsAction
				.setToolTipText("Identify Move Methods Opportunities");
		identifyBadSmellsAction.setImageDescriptor(PlatformUI.getWorkbench()
				.getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		identifyBadSmellsAction.setEnabled(false);

		saveResultsAction = new Action() {
			public void run() {
				saveResults();
			}
		};
		saveResultsAction.setToolTipText("Save Results");
		saveResultsAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_ETOOL_SAVE_EDIT));
		saveResultsAction.setEnabled(false);
		
		// APLICANDO REFATORAÇÂO
		applyRefactoringAction = new Action() {
			public void run() {
				IStructuredSelection selection = (IStructuredSelection) tableViewer
						.getSelection();
				Suggestion sug = (Suggestion) selection.getFirstElement();
				// CandidateRefactoring entry = (CandidateRefactoring) selection
				// .getFirstElement();

				IFile sourceFile = sug.getSourceIFile();
				IFile targetFile = sug.getTargetIFile();

				CompilationUnit sourceCompilationUnit = sug
						.getSourceCompilationUnit();

				CompilationUnit targetCompilationUnit = sug
						.getTargetClassCompilationUnit();

				Refactoring refactoring = null;

				// MoveMethodCandidateRefactoring candidate =
				// (MoveMethodCandidateRefactoring) entry;

				// refactoring = new MoveMethodRefactoring(
				// sourceCompilationUnit, targetCompilationUnit,
				// candidate.getSourceClassTypeDeclaration(),
				// candidate.getTargetClassTypeDeclaration(),
				// candidate.getSourceMethodDeclaration(),
				// candidate.getAdditionalMethodsToBeMoved(),
				// candidate.leaveDelegate(),
				// candidate.getMovedMethodName());

				refactoring = new MoveMethodRefactoring(sourceCompilationUnit,
						targetCompilationUnit,
						sug.recoverSourceClassTypeDeclaration(),
						sug.recovergetTargetClassTypeDeclaration(),
						sug.getSourceMethodDeclaration(),
						new HashMap<MethodInvocation, MethodDeclaration>(),
						false, sug.getMethodName());

				MyRefactoringWizard wizard = new MyRefactoringWizard(
						refactoring, applyRefactoringAction);

				RefactoringWizardOpenOperation op = new RefactoringWizardOpenOperation(
						wizard);
				try {
					String titleForFailedChecks = ""; //$NON-NLS-1$ 
					op.run(getSite().getShell(), titleForFailedChecks);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				try {
					IJavaElement targetJavaElement = JavaCore
							.create(targetFile);
					JavaUI.openInEditor(targetJavaElement);
					IJavaElement sourceJavaElement = JavaCore
							.create(sourceFile);
					JavaUI.openInEditor(sourceJavaElement);
				} catch (PartInitException e) {
					e.printStackTrace();
				} catch (JavaModelException e) {
					e.printStackTrace();
				}

			}
		};
		applyRefactoringAction.setToolTipText("Apply Refactoring");
		applyRefactoringAction.setImageDescriptor(PlatformUI.getWorkbench()
				.getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_DEF_VIEW));
		applyRefactoringAction.setEnabled(false);

		doubleClickAction = new Action() {
			public void run() {
				IStructuredSelection selection = (IStructuredSelection) tableViewer
						.getSelection();
				Suggestion sug = (Suggestion) selection.getFirstElement();
				// CandidateRefactoring candidate = (CandidateRefactoring)
				// selection
				// .getFirstElement();

				IFile sourceFile = sug.getSourceIFile();
				IFile targetFile = sug.getTargetIFile();
				try {
					IJavaElement targetJavaElement = JavaCore
							.create(targetFile);
					JavaUI.openInEditor(targetJavaElement);
					IJavaElement sourceJavaElement = JavaCore
							.create(sourceFile);
					ITextEditor sourceEditor = (ITextEditor) JavaUI
							.openInEditor(sourceJavaElement);
					List<Position> positions = sug.getPositions();
					AnnotationModel annotationModel = (AnnotationModel) sourceEditor
							.getDocumentProvider().getAnnotationModel(
									sourceEditor.getEditorInput());
					Iterator<Annotation> annotationIterator = annotationModel
							.getAnnotationIterator();
					while (annotationIterator.hasNext()) {
						Annotation currentAnnotation = annotationIterator
								.next();
						if (currentAnnotation.getType().equals(
								SliceAnnotation.EXTRACTION)) {
							annotationModel.removeAnnotation(currentAnnotation);
						}
					}
					for (Position position : positions) {
						SliceAnnotation annotation = new SliceAnnotation(
								SliceAnnotation.EXTRACTION,
								sug.getAnnotationText());
						annotationModel.addAnnotation(annotation, position);
					}
					Position firstPosition = positions.get(0);
					Position lastPosition = positions.get(positions.size() - 1);
					int offset = firstPosition.getOffset();
					int length = lastPosition.getOffset()
							+ lastPosition.getLength()
							- firstPosition.getOffset();
					sourceEditor.setHighlightRange(offset, length, true);
				} catch (PartInitException e) {
					e.printStackTrace();
				} catch (JavaModelException e) {
					e.printStackTrace();
				}

			}
		};
	}

	private void saveResults() {
		FileDialog fd = new FileDialog(getSite().getWorkbenchWindow().getShell(), SWT.SAVE);
		fd.setText("Save Results");
        String[] filterExt = { "*.txt" };
        fd.setFilterExtensions(filterExt);
        String selected = fd.open();
        if(selected != null) {
        	try {
        		BufferedWriter out = new BufferedWriter(new FileWriter(selected));
        		Table table = tableViewer.getTable();
        		TableColumn[] columns = table.getColumns();
        		for(int i=0; i<columns.length; i++) {
        			if(i == columns.length-1)
        				out.write(columns[i].getText());
        			else
        				out.write(columns[i].getText() + "\t");
        		}
        		out.newLine();
        		for(int i=0; i<table.getItemCount(); i++) {
        			TableItem tableItem = table.getItem(i);
        			for(int j=0; j<table.getColumnCount(); j++) {
        				if(j == table.getColumnCount()-1)
        					out.write(tableItem.getText(j));
        				else
        					out.write(tableItem.getText(j) + "\t");
        			}
        			out.newLine();
        		}
        		out.close();
        	} catch (IOException e) {
        		e.printStackTrace();
        	}
        }
	}
	
	public CandidateMap getTable() {
		// CandidateRefactoring[] table = null;
		//
		// try {
		// IWorkbench wb = PlatformUI.getWorkbench();
		// IProgressService ps = wb.getProgressService();

		// if (ASTReader.getSystemObject() != null
		// && selectedProject.equals(ASTReader.getExaminedProject())) {
		// ps.busyCursorWhile(new IRunnableWithProgress() {
		// public void run(IProgressMonitor monitor)
		// throws InvocationTargetException,
		// InterruptedException {
		// // new ASTReader(selectedProject,
		// // ASTReader.getSystemObject(), monitor);
		// map = (new ASTReader(selectedProject, ASTReader
		// .getSystemObject(), monitor)).getMap();
		// System.out.println("Valor do mapa2 " + map);
		// System.out.println("Valor do mapa3 "
		// + ASTReader.getMap());
		// }
		// });
		// } else {
		// ps.busyCursorWhile(new IRunnableWithProgress() {
		// public void run(IProgressMonitor monitor)
		// throws InvocationTargetException,
		// InterruptedException {
		// map = (new ASTReader(selectedProject, monitor))
		// .getMap();
		// System.out.println("Valor do mapa4 " + map);
		// System.out.println("Valor do mapa5 "
		// + ASTReader.getMap());
		// }
		// });
		// }
		// SystemObject systemObject = ASTReader.getSystemObject();
		// Set<ClassObject> classObjectsToBeExamined = new
		// LinkedHashSet<ClassObject>();
		// // if(selectedPackageFragmentRoot != null) {
		// //
		// classObjectsToBeExamined.addAll(systemObject.getClassObjects(selectedPackageFragmentRoot));
		// // }
		// // else if(selectedPackageFragment != null) {
		// //
		// classObjectsToBeExamined.addAll(systemObject.getClassObjects(selectedPackageFragment));
		// // }
		// // else if(selectedCompilationUnit != null) {
		// //
		// classObjectsToBeExamined.addAll(systemObject.getClassObjects(selectedCompilationUnit));
		// // }
		// // else if(selectedType != null) {
		// //
		// classObjectsToBeExamined.addAll(systemObject.getClassObjects(selectedType));
		// // }
		// // else {
		// classObjectsToBeExamined.addAll(systemObject.getClassObjects());
		// // }
		//
		// final Set<String> classNamesToBeExamined = new
		// LinkedHashSet<String>();
		// for (ClassObject classObject : classObjectsToBeExamined) {
		// classNamesToBeExamined.add(classObject.getName());
		// }
		// MySystem system = new MySystem(systemObject, false);
		//
		// // System.out.println("systemObject"+systemObject);
		//
		// final DistanceMatrix distanceMatrix = new DistanceMatrix(system);
		// ps.busyCursorWhile(new IRunnableWithProgress() {
		// public void run(IProgressMonitor monitor)
		// throws InvocationTargetException, InterruptedException {
		// distanceMatrix.generateDistances(monitor);
		// }
		// });
		//
		// final List<MoveMethodCandidateRefactoring> moveMethodCandidateList =
		// new ArrayList<MoveMethodCandidateRefactoring>();
		// // MARCADOR
		// ps.busyCursorWhile(new IRunnableWithProgress() {
		// public void run(IProgressMonitor monitor)
		// throws InvocationTargetException, InterruptedException {
		// System.out.println("classNamesToBeExamined"
		// + classNamesToBeExamined);
		// System.out.println("Mapa antes da chamada " + map);
		// moveMethodCandidateList.addAll(distanceMatrix
		// .getMoveMethodCandidateRefactoringsByAccess(
		// classNamesToBeExamined, map, monitor));
		// }
		// });
		//
		// System.out.println("moveMethodCandidateList");
		// for (MoveMethodCandidateRefactoring moveMethodCandidateRefactoring :
		// moveMethodCandidateList) {
		// System.out.println(moveMethodCandidateRefactoring);
		// }
		//
		// table = new CandidateRefactoring[moveMethodCandidateList.size() + 1];
		// table[0] = new CurrentSystem(distanceMatrix);
		// int counter = 1;
		// for (MoveMethodCandidateRefactoring candidate :
		// moveMethodCandidateList) {
		// table[counter] = candidate;
		// counter++;
		// }
		// } catch (InvocationTargetException e) {
		// e.printStackTrace();
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }

		System.out.println("Chamando Refine");
		Main jmove = new Main();// chamar com o nome do projeto
												// #selectedProject

		return jmove.execute(selectedProject);
	}

}
