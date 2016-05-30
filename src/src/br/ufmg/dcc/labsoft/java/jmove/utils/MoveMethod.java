package br.ufmg.dcc.labsoft.java.jmove.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.internal.corext.refactoring.structure.MoveInstanceMethodProcessor;
import org.eclipse.jdt.internal.ui.preferences.JavaPreferencesSettings;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.MoveRefactoring;

public class MoveMethod {

	// protected Refactoring getRefactoring(IMethod method)
	// throws OperationCanceledException, CoreException {
	//
	// MoveInstanceMethodProcessor processor = new MoveInstanceMethodProcessor(
	// method,
	// JavaPreferencesSettings.getCodeGenerationSettings(method
	// .getJavaProject()));
	//
	// IProgressMonitor progressMonitor = new NullProgressMonitor();
	// processor.checkInitialConditions(progressMonitor);
	//
	// IVariableBinding target = null;
	//
	// IVariableBinding[] targets = processor.getPossibleTargets();
	//
	// System.out.println("Target size " + targets.length);
	// for (int i = 0; i < targets.length; i++) {
	// IVariableBinding candidate = targets[i];
	// System.out.println("candidato "
	// + candidate.getDeclaringClass().getQualifiedName());
	// System.out
	// .println("name " + candidate.getType().getQualifiedName());
	// if (candidate.getName().equals("b")) {
	// target = candidate;
	//
	// // break;
	// }
	// }
	//
	// processor.setTarget(target);
	// processor.setInlineDelegator(true);
	// processor.setRemoveDelegator(true);
	// processor.setDeprecateDelegates(false);
	//
	// Refactoring ref = new MoveRefactoring(processor);
	// ref.checkInitialConditions(new NullProgressMonitor());
	//
	// return ref;
	// }

	// public void performRefactoring(IMethod method)
	// throws OperationCanceledException, CoreException {
	//
	// Refactoring refactoring = getRefactoring(method);
	//
	// RefactoringStatus status = refactoring
	// .checkAllConditions(new NullProgressMonitor());
	//
	// System.out.println(RefactoringStatus.OK);
	// if (status.getSeverity() != RefactoringStatus.OK) {
	// System.out.println("!ok :-(");
	// return;
	// }
	//
	// final CreateChangeOperation create = new CreateChangeOperation(
	// new CheckConditionsOperation(refactoring,
	// CheckConditionsOperation.ALL_CONDITIONS),
	// RefactoringStatus.FATAL);
	//
	// PerformChangeOperation perform = new PerformChangeOperation(create);
	//
	// IWorkspace workspace = ResourcesPlugin.getWorkspace();
	// // este comando aplica o refatoramento
	// workspace.run(perform, new NullProgressMonitor());
	//
	// // se precisar desfazer o refatoramento, pode usar o undo
	// Change undoChange = perform.getUndoChange();
	// RefactoringStatus conditionCheckingStatus = create
	// .getConditionCheckingStatus();
	// }

	public static List<String> getpossibleRefactoring(IMethod method) {

		List<String> candidatesList = new ArrayList<String>();

		try {

			System.out.println("Tentando method " + method.getElementName());
			MoveInstanceMethodProcessor processor = new MoveInstanceMethodProcessor(
					method,
					JavaPreferencesSettings.getCodeGenerationSettings(method
							.getJavaProject()));
			IProgressMonitor progressMonitor = new NullProgressMonitor();

			processor.checkInitialConditions(progressMonitor);

			IVariableBinding target = null;

			IVariableBinding[] targets = processor.getPossibleTargets();

			for (int i = 0; i < targets.length; i++) {
				IVariableBinding candidate = targets[i];
				// candidatesList.add(candidate.getType().getQualifiedName());

				processor = new MoveInstanceMethodProcessor(method,
						JavaPreferencesSettings
								.getCodeGenerationSettings(method
										.getJavaProject()));

				progressMonitor = new NullProgressMonitor();

				processor.checkInitialConditions(progressMonitor);

				processor.setTarget(candidate);
				processor.setInlineDelegator(true);
				processor.setRemoveDelegator(true);
				processor.setDeprecateDelegates(false);

				Refactoring ref = new MoveRefactoring(processor);
				RefactoringStatus status = null;

				status = ref.checkAllConditions(new NullProgressMonitor());

				if (status != null
						&& status.getSeverity() < RefactoringStatus.ERROR) {
					candidatesList.add(candidate.getType().getQualifiedName());
				}
			}

			treaty(candidatesList);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return candidatesList;
	}

	private static void treaty(List<String> candidatesList) {
		// TODO Auto-generated method stub

		ArrayList<String> toRemove = new ArrayList<String>();
		ArrayList<String> toAdd = new ArrayList<String>();

		for (String possibleCandidates : candidatesList) {

			int indexBegin = possibleCandidates.indexOf('<');

			if (indexBegin > 0) {
				String treatyName = possibleCandidates.substring(0, indexBegin);
				toRemove.add(possibleCandidates);
				toAdd.add(treatyName);

			}

			indexBegin = possibleCandidates.indexOf('[');
			if (indexBegin > 0) {
				String treatyName = possibleCandidates.substring(0, indexBegin);
				toRemove.add(possibleCandidates);
				toAdd.add(treatyName);

			}
		}

		candidatesList.removeAll(toRemove);
		candidatesList.addAll(toAdd);

	}

	public static boolean IsRefactoringPossible(IMethod method) {

		try {
			//
			// System.out.println("IsRefactoringPossible method " +
			// method.getElementName());
			MoveInstanceMethodProcessor processor = new MoveInstanceMethodProcessor(
					method,
					JavaPreferencesSettings.getCodeGenerationSettings(method
							.getJavaProject()));
			IProgressMonitor progressMonitor = new NullProgressMonitor();

			processor.checkInitialConditions(progressMonitor);

			IVariableBinding target = null;

			IVariableBinding[] targets = processor.getPossibleTargets();

			for (int i = 0; i < targets.length; i++) {
				IVariableBinding candidate = targets[i];
				// candidatesList.add(candidate.getType().getQualifiedName());

				processor = new MoveInstanceMethodProcessor(method,
						JavaPreferencesSettings
								.getCodeGenerationSettings(method
										.getJavaProject()));

				progressMonitor = new NullProgressMonitor();

				processor.checkInitialConditions(progressMonitor);

				processor.setTarget(candidate);
				processor.setInlineDelegator(true);
				processor.setRemoveDelegator(true);
				processor.setDeprecateDelegates(false);

				Refactoring ref = new MoveRefactoring(processor);
				RefactoringStatus status = null;

				status = ref.checkAllConditions(new NullProgressMonitor());

				if (status != null
						&& status.getSeverity() < RefactoringStatus.ERROR) {
					return true;
				}
			}

		} catch (Exception e) {;
			return false;
		}
		return false;

	}
}

// public static List<String> getpossibleRefactoring(IMethod method) {
//
// List<String> candidatesList = new ArrayList<String>();
//
// try {
//
// MoveInstanceMethodProcessor processor = new MoveInstanceMethodProcessor(
// method,
// JavaPreferencesSettings.getCodeGenerationSettings(method
// .getJavaProject()));
//
// IProgressMonitor progressMonitor = new NullProgressMonitor();
//
// processor.checkInitialConditions(progressMonitor);
//
// IVariableBinding[] targets = processor.getPossibleTargets();
//
// for (int i = 0; i < targets.length; i++) {
//
// IVariableBinding candidate = targets[i];
//
// processor = new MoveInstanceMethodProcessor(method,
// JavaPreferencesSettings
// .getCodeGenerationSettings(method
// .getJavaProject()));
//
// progressMonitor = new NullProgressMonitor();
//
// processor.checkInitialConditions(progressMonitor);
//
// processor.setTarget(candidate);
// processor.setInlineDelegator(true);
// processor.setRemoveDelegator(true);
// processor.setDeprecateDelegates(false);
// processor.setUseGetters(true);
//
// Refactoring ref = new MoveRefactoring(processor);
// RefactoringStatus status = null;
//
// status = ref.checkAllConditions(new NullProgressMonitor());
//
// if (status != null
// && status.getSeverity() < RefactoringStatus.ERROR) {
// candidatesList.add(candidate.getType().getQualifiedName());
// }
//
// }
// } catch (NullPointerException e) {
// // TODO Auto-generated catch block
// System.out.println("NullPointerException");
// } catch (Exception e) {
// // TODO: handle exception
// e.printStackTrace();
// }
//
// return candidatesList;
//
// }